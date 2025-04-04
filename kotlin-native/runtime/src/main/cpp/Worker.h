#ifndef RUNTIME_WORKER_H
#define RUNTIME_WORKER_H

#include "ExternalRCRef.hpp"
#include "Common.h"
#include "Types.h"

class Worker;

KInt GetWorkerId(Worker* worker);

Worker* WorkerInit(MemoryState* memoryState);
void WorkerDeinit(Worker* worker);
// Clean up all associated thread state, if this was a native worker.
void WorkerDestroyThreadDataIfNeeded(KInt id);
// Wait until all terminating native workers finish termination. Expected to be called at most once.
void WaitNativeWorkersTermination();
// Wait until terminating native worker `id` finishes termination. Expected to be called at most once for each worker.
void WaitNativeWorkerTermination(KInt id);
// Schedule the job without the result.
bool WorkerSchedule(KInt id, kotlin::mm::OwningExternalRCRef job);

#endif // RUNTIME_WORKER_H
