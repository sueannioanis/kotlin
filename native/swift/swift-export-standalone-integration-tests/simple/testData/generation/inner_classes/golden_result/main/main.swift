@_implementationOnly import KotlinBridges_main
import KotlinRuntime
import KotlinRuntimeSupport

public final class Outer: KotlinRuntime.KotlinBase, KotlinRuntimeSupport._KotlinBridged {
    public final class Inner: KotlinRuntime.KotlinBase, KotlinRuntimeSupport._KotlinBridged {
        public final class InnerInner: KotlinRuntime.KotlinBase, KotlinRuntimeSupport._KotlinBridged {
            package override init(
                __externalRCRef: Swift.UnsafeMutableRawPointer?
            ) {
                super.init(__externalRCRef: __externalRCRef)
            }
            public init(
                outer__: main.Outer.Inner
            ) {
                let __kt = Outer_Inner_InnerInner_init_allocate()
                super.init(__externalRCRef: __kt)
                Outer_Inner_InnerInner_init_initialize__TypesOfArguments__Swift_UnsafeMutableRawPointer_main_Outer_Inner__(__kt, outer__.__externalRCRef())
            }
        }
        package override init(
            __externalRCRef: Swift.UnsafeMutableRawPointer?
        ) {
            super.init(__externalRCRef: __externalRCRef)
        }
        public init(
            outer__: main.Outer
        ) {
            let __kt = Outer_Inner_init_allocate()
            super.init(__externalRCRef: __kt)
            Outer_Inner_init_initialize__TypesOfArguments__Swift_UnsafeMutableRawPointer_main_Outer__(__kt, outer__.__externalRCRef())
        }
        public func foo() -> Swift.Int32 {
            return Outer_Inner_foo(self.__externalRCRef())
        }
    }
    public override init() {
        let __kt = __root___Outer_init_allocate()
        super.init(__externalRCRef: __kt)
        __root___Outer_init_initialize__TypesOfArguments__Swift_UnsafeMutableRawPointer__(__kt)
    }
    package override init(
        __externalRCRef: Swift.UnsafeMutableRawPointer?
    ) {
        super.init(__externalRCRef: __externalRCRef)
    }
}
