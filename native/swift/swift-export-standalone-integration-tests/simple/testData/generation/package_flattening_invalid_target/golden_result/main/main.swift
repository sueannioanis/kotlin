@_exported import ExportedKotlinPackages
@_implementationOnly import KotlinBridges_main
import KotlinRuntime
import KotlinRuntimeSupport

public extension ExportedKotlinPackages.org.kotlin {
    public final class Foo: KotlinRuntime.KotlinBase, KotlinRuntimeSupport._KotlinBridged {
        public override init() {
            let __kt = org_kotlin_Foo_init_allocate()
            super.init(__externalRCRef: __kt)
            org_kotlin_Foo_init_initialize__TypesOfArguments__Swift_UnsafeMutableRawPointer__(__kt)
        }
        package override init(
            __externalRCRef: Swift.UnsafeMutableRawPointer?
        ) {
            super.init(__externalRCRef: __externalRCRef)
        }
    }
}
