package cn.afternode.d4jserver.plugin;

import cn.afternode.d4jserver.config.MavenRepositoryConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.maven.repository.internal.MavenRepositorySystemUtils;
import org.eclipse.aether.DefaultRepositorySystemSession;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.collection.CollectRequest;
import org.eclipse.aether.graph.Dependency;
import org.eclipse.aether.repository.LocalRepository;
import org.eclipse.aether.repository.RemoteRepository;
import org.eclipse.aether.resolution.ArtifactResult;
import org.eclipse.aether.resolution.DependencyRequest;
import org.eclipse.aether.resolution.DependencyResolutionException;
import org.eclipse.aether.resolution.DependencyResult;
import org.eclipse.aether.supplier.RepositorySystemSupplier;
import org.eclipse.aether.transfer.AbstractTransferListener;
import org.eclipse.aether.transfer.TransferCancelledException;
import org.eclipse.aether.transfer.TransferEvent;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

public class DependencyManager {
    private final Logger logger = LogManager.getLogger("DependencyManager");
    private final RepositorySystem system;
    private final DefaultRepositorySystemSession session;
    private final List<RemoteRepository> repositories;

    public DependencyManager(List<MavenRepositoryConfig> config) {
        system = new RepositorySystemSupplier().get();
        session = MavenRepositorySystemUtils.newSession();
        session.setChecksumPolicy("fail");
        session.setLocalRepositoryManager(system.newLocalRepositoryManager(session, new LocalRepository("libs")));
        session.setTransferListener(new AbstractTransferListener() {
            @Override
            public void transferStarted(TransferEvent event) {
                logger.info("Downloading " + event.getResource().getResourceName());
            }
        });
        session.setReadOnly();

        List<RemoteRepository> repos = new ArrayList<>();
        for (MavenRepositoryConfig cfg: config) {
            repos.add(new RemoteRepository.Builder(cfg.getId(), cfg.getType(), cfg.getUrl()).build());
        }
        repositories = system.newResolutionRepositories(session, repos);
        logger.info("Created DependencyManager with " + repositories.size() + " repositories");
    }

    public boolean resolve(ClassLoader loader, List<String> names, String task) {
        List<Dependency> deps = new ArrayList<>();
        for (String name: names) {
            deps.add(new Dependency(new DefaultArtifact(name), null));
        }

        DependencyResult result;
        try {
            result = system.resolveDependencies(session, new DependencyRequest(new CollectRequest((Dependency) null, deps, repositories), null));
        } catch (DependencyResolutionException e) {
            logger.warn("Error while resolving dependency in task " + task, e);
            return false;
        }

        List<URL> urls = new ArrayList<>();
        List<JarFile> files = new ArrayList<>();
        for (ArtifactResult ar: result.getArtifactResults()) {
            File f = ar.getArtifact().getFile();

            URL url;
            try {
                url = f.toURI().toURL();
            } catch (MalformedURLException e) {
                logger.warn("Cannot resolve URL of artifact " + ar.getArtifact().getArtifactId(), e);
                return false;
            }

            urls.add(url);
            try {
                files.add(new JarFile(f));
            } catch (IOException e) {
                logger.warn("Cannot resolve jar of artifact " + ar.getArtifact().getArtifactId(), e);
                return false;
            }
        }

        int loaded = 0;
        try (URLClassLoader ucl = new URLClassLoader(urls.toArray(new URL[0]), loader)) {
            for (JarFile jf: files) {
                Enumeration<? extends ZipEntry> entries = jf.entries();
                while (entries.hasMoreElements()) {
                    ZipEntry entry = entries.nextElement();
                    if (entry.getName().endsWith(".class") && !entry.getName().endsWith("module-info.class")) {
                        ucl.loadClass(entry.getName().split(".class")[0].replace("/", "."));
                    }
                }

                loaded ++;
                jf.close();
            }
        } catch (Throwable t) {
            logger.warn("Cannot load dependency in task " + task, t);
            return false;
        }
        if (loaded > 1) {
            logger.info("Loaded " + loaded + " dependencies in task " + task);
        } else {
            logger.info("Loaded " + loaded + " dependency in task " + task);
        }

        return true;
    }
}
