package org.gradle.accessors.dm;

import org.gradle.api.NonNullApi;
import org.gradle.api.artifacts.MinimalExternalModuleDependency;
import org.gradle.plugin.use.PluginDependency;
import org.gradle.api.artifacts.ExternalModuleDependencyBundle;
import org.gradle.api.artifacts.MutableVersionConstraint;
import org.gradle.api.provider.Provider;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.provider.ProviderFactory;
import org.gradle.api.internal.catalog.AbstractExternalDependencyFactory;
import org.gradle.api.internal.catalog.DefaultVersionCatalog;
import java.util.Map;
import org.gradle.api.internal.attributes.ImmutableAttributesFactory;
import org.gradle.api.internal.artifacts.dsl.CapabilityNotationParser;
import javax.inject.Inject;

/**
 * A catalog of dependencies accessible via the {@code libs} extension.
 */
@NonNullApi
public class LibrariesForLibsInPluginsBlock extends AbstractExternalDependencyFactory {

    private final AbstractExternalDependencyFactory owner = this;
    private final VersionAccessors vaccForVersionAccessors = new VersionAccessors(providers, config);
    private final BundleAccessors baccForBundleAccessors = new BundleAccessors(objects, providers, config, attributesFactory, capabilityNotationParser);
    private final PluginAccessors paccForPluginAccessors = new PluginAccessors(providers, config);

    @Inject
    public LibrariesForLibsInPluginsBlock(DefaultVersionCatalog config, ProviderFactory providers, ObjectFactory objects, ImmutableAttributesFactory attributesFactory, CapabilityNotationParser capabilityNotationParser) {
        super(config, providers, objects, attributesFactory, capabilityNotationParser);
    }

    /**
     * Dependency provider for <b>comFasterxmlJacksonCore</b> with <b>com.fasterxml.jackson.core:jackson-core</b> coordinates and
     * with version reference <b>comFasterxmlJacksonCore</b>
     * <p>
     * This dependency was declared in catalog libs.versions.toml
     *
     * @deprecated Will be removed in Gradle 9.0.
     */
    @Deprecated
    public Provider<MinimalExternalModuleDependency> getComFasterxmlJacksonCore() {
        org.gradle.internal.deprecation.DeprecationLogger.deprecateBehaviour("Accessing libraries or bundles from version catalogs in the plugins block.").withAdvice("Only use versions or plugins from catalogs in the plugins block.").willBeRemovedInGradle9().withUpgradeGuideSection(8, "kotlin_dsl_deprecated_catalogs_plugins_block").nagUser();
        return create("comFasterxmlJacksonCore");
    }

    /**
     * Dependency provider for <b>comFasterxmlJacksonCoreJacksonAnnotations</b> with <b>com.fasterxml.jackson.core:jackson-annotations</b> coordinates and
     * with version reference <b>comFasterxmlJacksonCoreJacksonAnnotations</b>
     * <p>
     * This dependency was declared in catalog libs.versions.toml
     *
     * @deprecated Will be removed in Gradle 9.0.
     */
    @Deprecated
    public Provider<MinimalExternalModuleDependency> getComFasterxmlJacksonCoreJacksonAnnotations() {
        org.gradle.internal.deprecation.DeprecationLogger.deprecateBehaviour("Accessing libraries or bundles from version catalogs in the plugins block.").withAdvice("Only use versions or plugins from catalogs in the plugins block.").willBeRemovedInGradle9().withUpgradeGuideSection(8, "kotlin_dsl_deprecated_catalogs_plugins_block").nagUser();
        return create("comFasterxmlJacksonCoreJacksonAnnotations");
    }

    /**
     * Dependency provider for <b>comFasterxmlJacksonCoreJacksonDatabind</b> with <b>com.fasterxml.jackson.core:jackson-databind</b> coordinates and
     * with version reference <b>comFasterxmlJacksonCoreJacksonDatabind</b>
     * <p>
     * This dependency was declared in catalog libs.versions.toml
     *
     * @deprecated Will be removed in Gradle 9.0.
     */
    @Deprecated
    public Provider<MinimalExternalModuleDependency> getComFasterxmlJacksonCoreJacksonDatabind() {
        org.gradle.internal.deprecation.DeprecationLogger.deprecateBehaviour("Accessing libraries or bundles from version catalogs in the plugins block.").withAdvice("Only use versions or plugins from catalogs in the plugins block.").willBeRemovedInGradle9().withUpgradeGuideSection(8, "kotlin_dsl_deprecated_catalogs_plugins_block").nagUser();
        return create("comFasterxmlJacksonCoreJacksonDatabind");
    }

    /**
     * Dependency provider for <b>comFasterxmlJacksonDatatypeJacksonDatatypeJsr310</b> with <b>com.fasterxml.jackson.datatype:jackson-datatype-jsr310</b> coordinates and
     * with version reference <b>comFasterxmlJacksonDatatypeJacksonDatatypeJsr310</b>
     * <p>
     * This dependency was declared in catalog libs.versions.toml
     *
     * @deprecated Will be removed in Gradle 9.0.
     */
    @Deprecated
    public Provider<MinimalExternalModuleDependency> getComFasterxmlJacksonDatatypeJacksonDatatypeJsr310() {
        org.gradle.internal.deprecation.DeprecationLogger.deprecateBehaviour("Accessing libraries or bundles from version catalogs in the plugins block.").withAdvice("Only use versions or plugins from catalogs in the plugins block.").willBeRemovedInGradle9().withUpgradeGuideSection(8, "kotlin_dsl_deprecated_catalogs_plugins_block").nagUser();
        return create("comFasterxmlJacksonDatatypeJacksonDatatypeJsr310");
    }

    /**
     * Dependency provider for <b>comGoogleGuavaGuava</b> with <b>com.google.guava:guava</b> coordinates and
     * with version reference <b>comGoogleGuavaGuava</b>
     * <p>
     * This dependency was declared in catalog libs.versions.toml
     *
     * @deprecated Will be removed in Gradle 9.0.
     */
    @Deprecated
    public Provider<MinimalExternalModuleDependency> getComGoogleGuavaGuava() {
        org.gradle.internal.deprecation.DeprecationLogger.deprecateBehaviour("Accessing libraries or bundles from version catalogs in the plugins block.").withAdvice("Only use versions or plugins from catalogs in the plugins block.").willBeRemovedInGradle9().withUpgradeGuideSection(8, "kotlin_dsl_deprecated_catalogs_plugins_block").nagUser();
        return create("comGoogleGuavaGuava");
    }

    /**
     * Dependency provider for <b>comSquareupOkhttp3Okhttp</b> with <b>com.squareup.okhttp3:okhttp</b> coordinates and
     * with version reference <b>comSquareupOkhttp3Okhttp</b>
     * <p>
     * This dependency was declared in catalog libs.versions.toml
     *
     * @deprecated Will be removed in Gradle 9.0.
     */
    @Deprecated
    public Provider<MinimalExternalModuleDependency> getComSquareupOkhttp3Okhttp() {
        org.gradle.internal.deprecation.DeprecationLogger.deprecateBehaviour("Accessing libraries or bundles from version catalogs in the plugins block.").withAdvice("Only use versions or plugins from catalogs in the plugins block.").willBeRemovedInGradle9().withUpgradeGuideSection(8, "kotlin_dsl_deprecated_catalogs_plugins_block").nagUser();
        return create("comSquareupOkhttp3Okhttp");
    }

    /**
     * Dependency provider for <b>commonsIoCommonsIo</b> with <b>commons-io:commons-io</b> coordinates and
     * with version reference <b>commonsIoCommonsIo</b>
     * <p>
     * This dependency was declared in catalog libs.versions.toml
     *
     * @deprecated Will be removed in Gradle 9.0.
     */
    @Deprecated
    public Provider<MinimalExternalModuleDependency> getCommonsIoCommonsIo() {
        org.gradle.internal.deprecation.DeprecationLogger.deprecateBehaviour("Accessing libraries or bundles from version catalogs in the plugins block.").withAdvice("Only use versions or plugins from catalogs in the plugins block.").willBeRemovedInGradle9().withUpgradeGuideSection(8, "kotlin_dsl_deprecated_catalogs_plugins_block").nagUser();
        return create("commonsIoCommonsIo");
    }

    /**
     * Dependency provider for <b>commonsLoggingCommonsLogging</b> with <b>commons-logging:commons-logging</b> coordinates and
     * with version reference <b>commonsLoggingCommonsLogging</b>
     * <p>
     * This dependency was declared in catalog libs.versions.toml
     *
     * @deprecated Will be removed in Gradle 9.0.
     */
    @Deprecated
    public Provider<MinimalExternalModuleDependency> getCommonsLoggingCommonsLogging() {
        org.gradle.internal.deprecation.DeprecationLogger.deprecateBehaviour("Accessing libraries or bundles from version catalogs in the plugins block.").withAdvice("Only use versions or plugins from catalogs in the plugins block.").willBeRemovedInGradle9().withUpgradeGuideSection(8, "kotlin_dsl_deprecated_catalogs_plugins_block").nagUser();
        return create("commonsLoggingCommonsLogging");
    }

    /**
     * Dependency provider for <b>junitJunit</b> with <b>junit:junit</b> coordinates and
     * with version reference <b>junitJunit</b>
     * <p>
     * This dependency was declared in catalog libs.versions.toml
     *
     * @deprecated Will be removed in Gradle 9.0.
     */
    @Deprecated
    public Provider<MinimalExternalModuleDependency> getJunitJunit() {
        org.gradle.internal.deprecation.DeprecationLogger.deprecateBehaviour("Accessing libraries or bundles from version catalogs in the plugins block.").withAdvice("Only use versions or plugins from catalogs in the plugins block.").willBeRemovedInGradle9().withUpgradeGuideSection(8, "kotlin_dsl_deprecated_catalogs_plugins_block").nagUser();
        return create("junitJunit");
    }

    /**
     * Dependency provider for <b>orgJetbrainsKotlinKotlinStdlib</b> with <b>org.jetbrains.kotlin:kotlin-stdlib</b> coordinates and
     * with version reference <b>orgJetbrainsKotlinKotlinStdlib</b>
     * <p>
     * This dependency was declared in catalog libs.versions.toml
     *
     * @deprecated Will be removed in Gradle 9.0.
     */
    @Deprecated
    public Provider<MinimalExternalModuleDependency> getOrgJetbrainsKotlinKotlinStdlib() {
        org.gradle.internal.deprecation.DeprecationLogger.deprecateBehaviour("Accessing libraries or bundles from version catalogs in the plugins block.").withAdvice("Only use versions or plugins from catalogs in the plugins block.").willBeRemovedInGradle9().withUpgradeGuideSection(8, "kotlin_dsl_deprecated_catalogs_plugins_block").nagUser();
        return create("orgJetbrainsKotlinKotlinStdlib");
    }

    /**
     * Dependency provider for <b>orgJetbrainsKotlinKotlinStdlibCommon</b> with <b>org.jetbrains.kotlin:kotlin-stdlib-common</b> coordinates and
     * with version reference <b>orgJetbrainsKotlinKotlinStdlibCommon</b>
     * <p>
     * This dependency was declared in catalog libs.versions.toml
     *
     * @deprecated Will be removed in Gradle 9.0.
     */
    @Deprecated
    public Provider<MinimalExternalModuleDependency> getOrgJetbrainsKotlinKotlinStdlibCommon() {
        org.gradle.internal.deprecation.DeprecationLogger.deprecateBehaviour("Accessing libraries or bundles from version catalogs in the plugins block.").withAdvice("Only use versions or plugins from catalogs in the plugins block.").willBeRemovedInGradle9().withUpgradeGuideSection(8, "kotlin_dsl_deprecated_catalogs_plugins_block").nagUser();
        return create("orgJetbrainsKotlinKotlinStdlibCommon");
    }

    /**
     * Dependency provider for <b>orgSlf4jSlf4jLog4j12</b> with <b>org.slf4j:slf4j-log4j12</b> coordinates and
     * with version reference <b>orgSlf4jSlf4jLog4j12</b>
     * <p>
     * This dependency was declared in catalog libs.versions.toml
     *
     * @deprecated Will be removed in Gradle 9.0.
     */
    @Deprecated
    public Provider<MinimalExternalModuleDependency> getOrgSlf4jSlf4jLog4j12() {
        org.gradle.internal.deprecation.DeprecationLogger.deprecateBehaviour("Accessing libraries or bundles from version catalogs in the plugins block.").withAdvice("Only use versions or plugins from catalogs in the plugins block.").willBeRemovedInGradle9().withUpgradeGuideSection(8, "kotlin_dsl_deprecated_catalogs_plugins_block").nagUser();
        return create("orgSlf4jSlf4jLog4j12");
    }

    /**
     * Dependency provider for <b>softwareAmazonAwsSdkBom</b> with <b>software.amazon.awssdk:bom</b> coordinates and
     * with version reference <b>softwareAmazonAwsSdkBom</b>
     * <p>
     * This dependency was declared in catalog libs.versions.toml
     *
     * @deprecated Will be removed in Gradle 9.0.
     */
    @Deprecated
    public Provider<MinimalExternalModuleDependency> getSoftwareAmazonAwsSdkBom() {
        org.gradle.internal.deprecation.DeprecationLogger.deprecateBehaviour("Accessing libraries or bundles from version catalogs in the plugins block.").withAdvice("Only use versions or plugins from catalogs in the plugins block.").willBeRemovedInGradle9().withUpgradeGuideSection(8, "kotlin_dsl_deprecated_catalogs_plugins_block").nagUser();
        return create("softwareAmazonAwsSdkBom");
    }

    /**
     * Group of versions at <b>versions</b>
     */
    public VersionAccessors getVersions() {
        return vaccForVersionAccessors;
    }

    /**
     * Group of bundles at <b>bundles</b>
     *
     * @deprecated Will be removed in Gradle 9.0.
     */
    @Deprecated
    public BundleAccessors getBundles() {
        org.gradle.internal.deprecation.DeprecationLogger.deprecateBehaviour("Accessing libraries or bundles from version catalogs in the plugins block.").withAdvice("Only use versions or plugins from catalogs in the plugins block.").willBeRemovedInGradle9().withUpgradeGuideSection(8, "kotlin_dsl_deprecated_catalogs_plugins_block").nagUser();
        return baccForBundleAccessors;
    }

    /**
     * Group of plugins at <b>plugins</b>
     */
    public PluginAccessors getPlugins() {
        return paccForPluginAccessors;
    }

    public static class VersionAccessors extends VersionFactory  {

        public VersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Version alias <b>comFasterxmlJacksonCore</b> with value <b>2.18.0</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getComFasterxmlJacksonCore() { return getVersion("comFasterxmlJacksonCore"); }

        /**
         * Version alias <b>comFasterxmlJacksonCoreJacksonAnnotations</b> with value <b>2.18.0</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getComFasterxmlJacksonCoreJacksonAnnotations() { return getVersion("comFasterxmlJacksonCoreJacksonAnnotations"); }

        /**
         * Version alias <b>comFasterxmlJacksonCoreJacksonDatabind</b> with value <b>2.18.0</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getComFasterxmlJacksonCoreJacksonDatabind() { return getVersion("comFasterxmlJacksonCoreJacksonDatabind"); }

        /**
         * Version alias <b>comFasterxmlJacksonDatatypeJacksonDatatypeJsr310</b> with value <b>2.18.0</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getComFasterxmlJacksonDatatypeJacksonDatatypeJsr310() { return getVersion("comFasterxmlJacksonDatatypeJacksonDatatypeJsr310"); }

        /**
         * Version alias <b>comGoogleGuavaGuava</b> with value <b>33.3.1-jre</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getComGoogleGuavaGuava() { return getVersion("comGoogleGuavaGuava"); }

        /**
         * Version alias <b>comSquareupOkhttp3Okhttp</b> with value <b>4.12.0</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getComSquareupOkhttp3Okhttp() { return getVersion("comSquareupOkhttp3Okhttp"); }

        /**
         * Version alias <b>commonsIoCommonsIo</b> with value <b>2.16.1</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getCommonsIoCommonsIo() { return getVersion("commonsIoCommonsIo"); }

        /**
         * Version alias <b>commonsLoggingCommonsLogging</b> with value <b>1.3.2</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getCommonsLoggingCommonsLogging() { return getVersion("commonsLoggingCommonsLogging"); }

        /**
         * Version alias <b>junitJunit</b> with value <b>4.13.2</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getJunitJunit() { return getVersion("junitJunit"); }

        /**
         * Version alias <b>orgJetbrainsKotlinKotlinStdlib</b> with value <b>2.1.0</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getOrgJetbrainsKotlinKotlinStdlib() { return getVersion("orgJetbrainsKotlinKotlinStdlib"); }

        /**
         * Version alias <b>orgJetbrainsKotlinKotlinStdlibCommon</b> with value <b>2.1.0</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getOrgJetbrainsKotlinKotlinStdlibCommon() { return getVersion("orgJetbrainsKotlinKotlinStdlibCommon"); }

        /**
         * Version alias <b>orgSlf4jSlf4jLog4j12</b> with value <b>2.0.9</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getOrgSlf4jSlf4jLog4j12() { return getVersion("orgSlf4jSlf4jLog4j12"); }

        /**
         * Version alias <b>softwareAmazonAwsSdkBom</b> with value <b>2.20.10</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getSoftwareAmazonAwsSdkBom() { return getVersion("softwareAmazonAwsSdkBom"); }

    }

    /**
     * @deprecated Will be removed in Gradle 9.0.
     */
    @Deprecated
    public static class BundleAccessors extends BundleFactory {

        public BundleAccessors(ObjectFactory objects, ProviderFactory providers, DefaultVersionCatalog config, ImmutableAttributesFactory attributesFactory, CapabilityNotationParser capabilityNotationParser) { super(objects, providers, config, attributesFactory, capabilityNotationParser); }

    }

    public static class PluginAccessors extends PluginFactory {

        public PluginAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

    }

}
