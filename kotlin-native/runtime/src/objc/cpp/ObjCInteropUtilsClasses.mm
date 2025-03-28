/*
 * Copyright 2010-2019 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license
 * that can be found in the LICENSE file.
 */

#if KONAN_OBJC_INTEROP

#import <objc/runtime.h>
#import <Foundation/Foundation.h>

#include "ExternalRCRef.hpp"
#import "Memory.h"
#import "ObjCExportPrivate.h"
#import "ObjCInteropUtilsPrivate.h"

using namespace kotlin;

// TODO: rework the interface to reduce the number of virtual calls
// in Kotlin_Interop_createKotlinObjectHolder and Kotlin_Interop_unwrapKotlinObjectHolder
@interface KotlinObjectHolder : NSObject
-(id)initWithRef:(KRef)ref;
-(KRef)ref;
@end

@implementation KotlinObjectHolder {
  mm::OwningExternalRCRef refHolder;
};

-(id)initWithRef:(KRef)ref {
  if (self = [super init]) {
    refHolder.reset(ref);
  }
  return self;
}

-(KRef)ref {
  return refHolder.ref();
}

@end

static id Kotlin_Interop_createKotlinObjectHolder(KRef any) {
  if (any == nullptr) {
    return nullptr;
  }

  return [[[KotlinObjectHolder alloc] initWithRef:any] autorelease];
}

static KRef Kotlin_Interop_unwrapKotlinObjectHolder(id holder) {
  if (holder == nullptr) {
    return nullptr;
  }

  return [((KotlinObjectHolder*)holder) ref];
}

// Used as an associated object for ObjCWeakReferenceImpl.
@interface KotlinObjCWeakReference : NSObject
@end

// libobjc:
extern "C" {
id objc_loadWeakRetained(id *location);
id objc_storeWeak(id *location, id newObj);
void objc_destroyWeak(id *location);
void objc_release(id obj);
}

@implementation KotlinObjCWeakReference {
  @public id referred;
}

// Called when removing Kotlin object.
-(void)releaseAsAssociatedObject {
  objc_destroyWeak(&referred);
  objc_release(self);
}

@end

extern "C" OBJ_GETTER(Kotlin_Interop_refFromObjC, id obj);

static OBJ_GETTER(Konan_ObjCInterop_getWeakReference, KRef ref) {
  KotlinObjCWeakReference* objcRef = (KotlinObjCWeakReference*)ref->GetAssociatedObject();

  // objc_loadWeakRetained can call arbitrary user code, so it needs Native state:
  id objcReferred = kotlin::CallWithThreadState<kotlin::ThreadState::kNative>(objc_loadWeakRetained, &objcRef->referred);

  // Kotlin_Interop_refFromObjC creates Kotlin objects, so it needs Runnable state:
  KRef result = Kotlin_Interop_refFromObjC(objcReferred, OBJ_RESULT);

  // objc_release can call arbitrary user code, so it needs Native state:
  kotlin::CallWithThreadState<kotlin::ThreadState::kNative>(objc_release, objcReferred);

  // Possible optimizations for thread state switching above:
  //  1. `Kotlin_Interop_refFromObjC` typically does `objc_retain` under the hood. We could coalesce it with `objc_release` above.
  //  2. `objc_loadWeakRetained` and `objc_release` typically shouldn't call arbitrary user code here:
  //     the latter can't deallocate the object because it is still alive by this point, so the only possibility for user code here is
  //     when a user overrides `_tryRetain` or `release`.
  //     We do this for Kotlin subclasses of Obj-C classes, which also use this implementation.
  //     But we could use the other weak implementation for such classes, and assume no one else overrides these methods
  //     (in a dangerous way).

  return result;
}

static void Konan_ObjCInterop_initWeakReference(KRef ref, id objcPtr) {
  KotlinObjCWeakReference* objcRef = [KotlinObjCWeakReference new];
  objc_storeWeak(&objcRef->referred, objcPtr);
  ref->SetAssociatedObject(objcRef);
}

__attribute__((constructor))
static void injectToRuntime() {
  RuntimeCheck(Kotlin_Interop_createKotlinObjectHolder_ptr == nullptr, "runtime injected twice");
  Kotlin_Interop_createKotlinObjectHolder_ptr = &Kotlin_Interop_createKotlinObjectHolder;

  RuntimeCheck(Kotlin_Interop_unwrapKotlinObjectHolder_ptr == nullptr, "runtime injected twice");
  Kotlin_Interop_unwrapKotlinObjectHolder_ptr = &Kotlin_Interop_unwrapKotlinObjectHolder;

  RuntimeCheck(Konan_ObjCInterop_getWeakReference_ptr == nullptr, "runtime injected twice");
  Konan_ObjCInterop_getWeakReference_ptr = &Konan_ObjCInterop_getWeakReference;

  RuntimeCheck(Konan_ObjCInterop_initWeakReference_ptr == nullptr, "runtime injected twice");
  Konan_ObjCInterop_initWeakReference_ptr = &Konan_ObjCInterop_initWeakReference;
}

#endif // KONAN_OBJC_INTEROP
