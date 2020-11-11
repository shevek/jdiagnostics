/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.jdiagnostics;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;
import java.util.jar.Attributes;
import java.util.jar.Manifest;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Product Metadata and version information.
 *
 * This class is driven by metadata inserted into JAR files by the Netflix
 * gradle-info-plugin, Maven Plexus or Bnd plugins, or similar.
 *
 * Any module built with appropriate metadata in the MANIFEST.MF will show up
 * regardless of whether it is officially a component of the package software
 * or merely a third party dependency.
 *
 * @author shevek
 */
public class ProductMetadata {

    // private static final Logger LOG = LoggerFactory.getLogger(ProductMetadata.class);
    @Documented
    @Retention(RetentionPolicy.CLASS)
    private static @interface Nonnull {
    }

    @Documented
    @Retention(RetentionPolicy.CLASS)
    private static @interface CheckForNull {
    }

    @Nonnull
    private static Logger getLogger() {
        return Logger.getLogger(ProductMetadata.class.getName());
    }

    /**
     * A non-exhaustive list of well-known keys which appear in metadata.
     */
    public static enum MetadataProperty {

        Archiver_Version("Archiver-Version"),
        Bnd_LastModified("Bnd-LastModified"),
        Branch("Branch"),
        Build_Date("Build-Date"),
        Build_Host("Build-Host"),
        Build_Id("Build-Id"),
        Build_Java_Version("Build-Java-Version"),
        Build_Jdk("Build-Jdk"),
        Build_Job("Build-Job"),
        Build_Number("Build-Number"),
        Built_By("Built-By"),
        Built_OS("Built-OS"),
        Built_Status("Built-Status"),
        Bundle_Description("Bundle-Description"),
        Bundle_DocURL("Bundle-DocURL"),
        Bundle_License("Bundle-License"),
        Bundle_ManifestVersion("Bundle-ManifestVersion"),
        Bundle_Name("Bundle-Name"),
        Bundle_RequiredExecutionEnvironment("Bundle-RequiredExecutionEnvironment"),
        Bundle_SymbolicName("Bundle-SymbolicName"),
        Bundle_Vendor("Bundle-Vendor"),
        Bundle_Version("Bundle-Version"),
        Change("Change"),
        Created_By("Created-By"),
        Gradle_Version("Gradle-Version"),
        Implementation_Title("Implementation-Title"),
        Implementation_Vendor("Implementation-Vendor"),
        Implementation_Vendor_Id("Implementation-Vendor-Id"),
        Implementation_Version("Implementation-Version"),
        Manifest_Version("Manifest-Version"),
        Module_Origin("Module-Origin"),
        Module_Source("Module-Source"),
        X_Compile_Source_JDK("X-Compile-Source-JDK"),
        X_Compile_Target_JDK("X-Compile-Target-JDK"),;
        private final String key;

        private MetadataProperty(@Nonnull String key) {
            this.key = key;
        }

        @Nonnull
        public String getKey() {
            return key;
        }

        @Override
        public String toString() {
            return getKey();
        }
    }

    public static class ModuleVersion {

        private final String group;
        private final String name;
        private final String version;

        public ModuleVersion(@CheckForNull String group, @Nonnull String name, @Nonnull String version) {
            this.group = group;
            this.name = name;
            this.version = version;
        }

        /**
         * Returns what is probably the Maven group name of the artifact.
         *
         * @return what is probably the Maven group name of the artifact.
         */
        @CheckForNull
        public String getGroup() {
            return group;
        }

        /**
         * Returns the name of the artifact, which might also be the maven artifact name.
         *
         * @return the name of the artifact, which might also be the maven artifact name.
         */
        @Nonnull
        public String getName() {
            return name;
        }

        @Nonnull
        public String getVersion() {
            return version;
        }

        private void toName(@Nonnull StringBuilder buf) {
            if (group != null)
                buf.append(group).append(':');
            buf.append(name);
        }

        public String toName() {
            StringBuilder buf = new StringBuilder();
            toName(buf);
            return buf.toString();
        }

        @Override
        public String toString() {
            StringBuilder buf = new StringBuilder();
            toName(buf);
            buf.append(':').append(version);
            return buf.toString();
        }

        /** Parses Implementation-Title from gradle-info-plugin. */
        @CheckForNull
        public static ModuleVersion forGradleInfo(@Nonnull Attributes attributes) {
            String implementationTitle = attributes.getValue(MetadataProperty.Implementation_Title.key);
            if (implementationTitle == null)
                return null;
            int hashIdx = implementationTitle.indexOf('#');
            if (hashIdx == -1)
                return null;    // Not from Netflix plugin.
            String group = implementationTitle.substring(0, hashIdx);
            implementationTitle = implementationTitle.substring(hashIdx + 1);
            int semiIdx = implementationTitle.indexOf(';');
            if (semiIdx == -1)
                return null;
            String version = implementationTitle.substring(semiIdx + 1);
            implementationTitle = implementationTitle.substring(0, semiIdx);
            return new ModuleVersion(group, implementationTitle, version);
        }

        @CheckForNull
        public static ModuleVersion forMavenPlexus(@Nonnull Attributes attributes) {
            String implementationTitle = attributes.getValue(MetadataProperty.Implementation_Title.key);
            if (implementationTitle == null)
                return null;
            String implementationVersion = attributes.getValue(MetadataProperty.Implementation_Version.key);
            if (implementationVersion == null)
                return null;
            String implementationVendor = attributes.getValue(MetadataProperty.Implementation_Vendor_Id.key);
            return new ModuleVersion(implementationVendor, implementationTitle, implementationVersion);
        }

        /** Parses Bundle-SymbolicName and Bundle-Version from Maven Bundle Plugin. */
        @CheckForNull
        public static ModuleVersion forMavenBundle(@Nonnull Attributes attributes) {
            String bundleSymbolicName = attributes.getValue(MetadataProperty.Bundle_SymbolicName.key);
            if (bundleSymbolicName == null)
                return null;
            String bundleVersion = attributes.getValue(MetadataProperty.Bundle_Version.key);
            if (bundleVersion == null)
                return null;
            return new ModuleVersion(null, bundleSymbolicName, bundleVersion);
        }

        @CheckForNull
        public static ModuleVersion forAttributes(@Nonnull Attributes attributes) {
            // We can't guess the private resource name except by parsing the manifest.
            // Luckily, Netflix puts the metadata into both places.
            ModuleVersion moduleVersion;
            moduleVersion = forGradleInfo(attributes);
            if (moduleVersion != null)
                return moduleVersion;

            // However, some OTHER people also write incompatible data into the manifest.
            moduleVersion = forMavenPlexus(attributes);
            if (moduleVersion != null)
                return moduleVersion;

            moduleVersion = forMavenBundle(attributes);
            if (moduleVersion != null)
                return moduleVersion;

            return null;
        }
    }

    public static class ModuleMetadata {

        private final ProductMetadata productMetadata;
        private final ModuleVersion moduleVersion;
        private final URL manifestUrl;
        private final Manifest manifest;
        private final Properties properties = new Properties();

        public ModuleMetadata(
                @Nonnull ProductMetadata productMetadata,
                @Nonnull ModuleVersion moduleVersion,
                @CheckForNull URL manifestUrl,
                @Nonnull Manifest manifest) {
            this.productMetadata = productMetadata;
            this.moduleVersion = moduleVersion;
            this.manifestUrl = manifestUrl;
            this.manifest = manifest;
        }

        @Nonnull
        public ProductMetadata getProductMetadata() {
            return productMetadata;
        }

        @Nonnull
        public ModuleVersion getModuleVersion() {
            return moduleVersion;
        }

        /**
         * Returns the name of this module.
         *
         * There is no particularly standard format for names; it might be
         * a Maven coordinate name, or an OSGI name, or might even contain
         * spaces. Caveat caller.
         *
         * @return the name of this module.
         */
        @Nonnull
        public String getName() {
            return getModuleVersion().toName();
        }

        /**
         * Returns the version of this module.
         *
         * @return the version of this module.
         */
        @Nonnull
        public String getVersion() {
            return getModuleVersion().getVersion();
        }

        @Nonnull
        public Manifest getManifest() {
            return manifest;
        }

        @Nonnull
        public Properties getProperties() {
            return properties;
        }

        @CheckForNull
        public File getJar() {
            if (manifestUrl == null)
                return null;
            String resourcePath = manifestUrl.toString();
            if (!resourcePath.startsWith("jar:file:"))
                return null;
            resourcePath = resourcePath.substring(9);
            int idx = resourcePath.indexOf('!');
            if (idx > 0)
                resourcePath = resourcePath.substring(0, idx);
            return new File(resourcePath.replace('/', File.separatorChar));
        }

        /**
         * Returns the value for the given key, either from the properties or the manifest's main-attributes.
         *
         * @param key The key to look up.
         * @return The value, or null.
         */
        @CheckForNull
        public String getMetadataProperty(@Nonnull String key) {
            String value = properties.getProperty(key);
            if (value == null)
                value = manifest.getMainAttributes().getValue(key);
            return value;
        }

        /**
         * Returns the value for the given key, either from the properties or the manifest's main-attributes.
         *
         * @param key The key to look up.
         * @return The value, or null.
         */
        @CheckForNull
        public String getMetadataProperty(@Nonnull MetadataProperty key) {
            return getMetadataProperty(key.key);
        }

        /** @return The VCS revision of this module, as in Maven metadata. */
        @CheckForNull
        public String getRevision() {
            return getMetadataProperty(MetadataProperty.Change);
        }

        @CheckForNull
        public String getBuildBranch() {
            return getMetadataProperty(MetadataProperty.Branch);
        }

        @CheckForNull
        public String getBuildDate() {
            // Build-Date=2015-01-01_10:09:09
            // String text = getMetadataProperty("Build-Date");
            // SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
            return getMetadataProperty(MetadataProperty.Build_Date);
        }

        /**
         * Returns a summary of the version of this product.
         *
         * This is almost always useful.
         *
         * This is derived from {@link #getModuleVersion()},
         * {@link #getBuildBranch()},
         * {@link #getRevision()},
         * and {@link #getBuildDate()}.
         *
         * @return a summary of the circumstances under which this product was built.
         */
        @Nonnull
        public String getSummary() {
            StringBuilder buf = new StringBuilder();
            buf.append(getModuleVersion());
            String branch = getBuildBranch();
            if (branch != null && !"master".equals(branch))
                buf.append(" (branch ").append(branch).append(")");
            String revision = getRevision();
            if (revision != null)
                buf.append(" (rev ").append(revision).append(")");
            String buildDate = getBuildDate();
            if (buildDate != null)
                buf.append(" built on ").append(buildDate);
            return buf.toString();
        }

        /** Returns ${JENKINS_URL} (Jenkins) or equivalent from the build system. */
        @CheckForNull
        public String getBuildHost() {
            return getMetadataProperty(MetadataProperty.Build_Host);
        }

        /** Returns ${JOB_NAME} (Jenkins) or equivalent from the build system. */
        @CheckForNull
        public String getBuildJob() {
            return getMetadataProperty(MetadataProperty.Build_Job);
        }

        /** Returns ${BUILD_NUMBER} (Jenkins) or equivalent from the build system. */
        @CheckForNull
        public String getBuildNumber() {
            return getMetadataProperty(MetadataProperty.Build_Number);
        }

        /**
         * Returns a summary of the circumstances under which this product was built.
         *
         * This is most likely to be useful for JARs built using a CI provider
         * and the nebula-info Gradle plugins.
         *
         * This is derived from {@link MetadataProperty#Build_Host},
         * {@link MetadataProperty#Build_Job} and
         * {@link MetadataProperty#Build_Number}.
         *
         * @return a summary of the circumstances under which this product was built.
         */
        @Nonnull
        public String getBuildSummary() {
            StringBuilder buf = new StringBuilder();
            buf.append("Built by ");
            String buildHost = getBuildHost();
            if (buildHost != null)
                buf.append(buildHost);
            String buildJob = getBuildJob();
            if (buildJob != null)
                buf.append(":").append(buildJob);
            String buildNumber = getBuildNumber();
            if (buildNumber != null)
                buf.append("#").append(buildNumber);
            return buf.toString();
        }

        @Override
        public String toString() {
            return getSummary();
        }
    }

    @Nonnull
    private static ClassLoader getDefaultClassLoader() {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        if (loader != null)
            return loader;
        return ProductMetadata.class.getClassLoader();
    }

    @CheckForNull
    private static Manifest newManifest(@Nonnull URL resource) {
        try {
            InputStream in = resource.openStream();
            try {
                return new Manifest(in);
            } finally {
                in.close();
            }
        } catch (Exception e) {
            // This should never happen because we previously discovered the manifest as a resource.
            // However, if we replace a JAR underneath a running JVM, we can get FileNotFoundException.
            getLogger().log(Level.WARNING, "Failed to load Manifest from " + resource, e);
            return null;
        }
    }

    private final ClassLoader loader;
    private final Map<String, ModuleMetadata> modules = new TreeMap<String, ModuleMetadata>();

    /**
     * Creates a new ProductMetadata.
     *
     * @param loader The ClassLoader which can access the product resources.
     * @throws IOException Thrown only if the "global" load fails, making this object useless. The failure of any module will be ignored.
     */
    public ProductMetadata(@Nonnull ClassLoader loader) throws IOException {
        this.loader = loader;
        load();
    }

    /**
     * Creates a new ProductMetadata.
     *
     * @throws IOException Thrown only if the "global" load fails, making this object useless. The failure of any module will be ignored.
     */
    public ProductMetadata() throws IOException {
        this(getDefaultClassLoader());
    }

    @CheckForNull
    private ModuleMetadata newModule(@Nonnull URL manifestUrl, @Nonnull Manifest manifest, @Nonnull ModuleVersion version) {
        // URL resource = loader.getResource("META-INF/" + version.getName() + ".properties");
        ModuleMetadata module = new ModuleMetadata(this, version, manifestUrl, manifest);
        String spec = "/META-INF/" + version.getName() + ".properties";
        try {
            URL propertiesUrl = new URL(manifestUrl, spec);
            // getLogger().info("Loading " + propertiesUrl);
            try {
                InputStream in = propertiesUrl.openStream();
                try {
                    module.properties.load(in);
                } finally {
                    in.close();
                }
                // LOG.debug("Loaded " + module + " from " + resource);   // Might throw.
            } catch (FileNotFoundException e) {
                // getLogger().log(Level.WARNING, "Failed to load ModuleMetadata from " + spec, e);
            }
        } catch (Exception e) {
            getLogger().log(Level.WARNING, "Failed to load ModuleMetadata for " + version + " from " + spec, e);
        }
        return module;
    }

    private void load() throws IOException {
        // This is a cheap way to discover JAR files.
        Enumeration<URL> manifestUrls = loader.getResources("META-INF/MANIFEST.MF");
        for (URL manifestUrl : Collections.list(manifestUrls)) {
            // System.err.println("Found " + resource);
            Manifest manifest = newManifest(manifestUrl);
            if (manifest == null)
                continue;
            Attributes attributes = manifest.getMainAttributes();
            if (attributes == null)
                continue;
            ModuleVersion moduleVersion = ModuleVersion.forAttributes(attributes);
            if (moduleVersion == null)
                continue;
            ModuleMetadata moduleMetadata = newModule(manifestUrl, manifest, moduleVersion);
            if (moduleMetadata == null)
                continue;
            modules.put(moduleMetadata.getName(), moduleMetadata);
        }
    }

    /**
     * Returns the ModuleMetadata for all discovered modules.
     *
     * @return the ModuleMetadata for all discovered modules.
     */
    @Nonnull
    public Collection<ModuleMetadata> getModules() {
        return new ArrayList<ModuleMetadata>(modules.values());
    }

    /**
     * Retrieves the metadata for a module by name.
     *
     * Names are not particularly consistent - the best names are
     * Maven coordinates derived from packages built using the
     * gradle-info-plugin, but tolerable names
     * are derived from OSGI packageers like Maven Plexus or Bnd plugins.
     *
     * @param name The module name, e.g. "org.anarres.jdiagnostics:jdiagnostics" or "slf4j-api".
     * @return The ModuleMetadata, if found, else null.
     */
    @CheckForNull
    public ModuleMetadata getModule(@Nonnull String name) {
        return modules.get(name);
    }

    /**
     * A utility for producing a support text listing only modules
     * within the product.
     *
     * For example, toString("org.anarres.") will include all modules
     * containing metadata with a name starting with "org.anarres." but
     * exclude all others.
     *
     * @param namePrefix The prefix to apply to {@link ModuleMetadata#getName()}.
     * @return An equivalent of {@link #toString()} with only requested modules.
     */
    @Nonnull
    public String toString(String namePrefix) {
        StringBuilder buf = new StringBuilder();
        for (ModuleMetadata module : getModules())
            if (module.getName().startsWith(namePrefix))
                buf.append(module).append("\n");
        return buf.toString();
    }

    @Override
    public String toString() {
        return toString("");
    }

}
