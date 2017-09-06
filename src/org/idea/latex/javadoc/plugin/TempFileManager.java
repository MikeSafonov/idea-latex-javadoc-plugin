/*
 * Copyright 2013 Raffael Herzog
 *
 * This file is part of markdown-doclet.
 *
 * markdown-doclet is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * markdown-doclet is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with markdown-doclet.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.idea.latex.javadoc.plugin;

import com.google.common.hash.Hashing;
import com.google.common.util.concurrent.AbstractScheduledService;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.intellij.openapi.application.PathManager;
import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.util.io.FileUtil;
import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


/**
 * An application component that manages temporary files used to display LaTeX formulas
 * in QuickDoc. It saves the content to a file
 * `system/plugins/latex-javadoc/<hash>.<extension>` and deletes that file after it
 * hasn't been used for a while. Also cleans up the temporary directory on IDE startup
 * and shutdown.
 */
public class TempFileManager implements ApplicationComponent {

    private static final long CLEANUP_RATE = 1;
    private static final TimeUnit CLEANUP_RATE_UNIT = TimeUnit.MINUTES;
    private static final long MAX_AGE = TimeUnit.MINUTES.toMillis(1);

    private File baseDir;
    private final AbstractScheduledService cleanupService = new AbstractScheduledService() {
        @Override
        protected void runOneIteration() throws Exception {
            File[] files;
            synchronized (TempFileManager.this) {
                files = baseDir.listFiles();
            }
            if (files != null) {
                for (File file : files) {
                    synchronized (index) {
                        Long timestamp = index.get(file.getName());
                        if (timestamp == null || timestamp < (System.currentTimeMillis() - MAX_AGE)) {
                            FileUtil.delete(file);
                            index.remove(file.getName());
                        }
                    }
                }
            }
        }

        @Override
        protected Scheduler scheduler() {
            return Scheduler.newFixedDelaySchedule(CLEANUP_RATE, CLEANUP_RATE, CLEANUP_RATE_UNIT);
        }

        @Override
        protected ScheduledExecutorService executor() {
            return new ScheduledThreadPoolExecutor(
                    1, new ThreadFactoryBuilder()
                    .setDaemon(true)
                    .setNameFormat(getComponentName() + " Temp File Cleanup")
                    .build());
        }
    };
    private final Map<String, Long> index = new HashMap<>();

    public TempFileManager() {
    }

    @Override
    public synchronized void initComponent() {
        baseDir = new File(PathManager.getPluginTempPath(), "latex-javadoc");
        cleanup();
        cleanupService.startAsync().awaitRunning();
    }

    @Override
    public synchronized void disposeComponent() {
        cleanupService.stopAsync().awaitTerminated();
        cleanup();
    }


    public synchronized URL saveTempFile(byte[] formulaBytes, BufferedImage image, String extension) throws IOException {

        String name = Hashing.sha1().hashBytes(formulaBytes).toString();
        File tempFile = new File(baseDir, name + "." + extension);
        URL fileUrl = null;
        synchronized (index) {

            if (tempFile.exists()) {
                index.put(name, System.currentTimeMillis());
                fileUrl = tempFile.toURI().toURL();
            }

            if (!tempFile.isFile()) {
                boolean fileCreated = ImageIO.write(image, "png", tempFile);
                if (fileCreated) {
                    index.put(name, System.currentTimeMillis());
                    fileUrl = tempFile.toURI().toURL();
                }
            }

        }

        return fileUrl;
    }

    void cleanup() {

        System.out.println("cleanup");
        if (FileUtil.createDirectory(baseDir)) {
            System.out.println("deleting all files");
            for (File child : baseDir.listFiles()) {
                FileUtil.delete(child);

            }
        }

        synchronized (index) {
            System.out.println("deleting all index");
            index.clear();
        }
    }

    @Override
    @NotNull
    public String getComponentName() {
        return Plugin.TEMP_FILE_MANAGER_NAME;
    }
}
