package com;

import org.apache.commons.io.FileUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.LocatedFileStatus;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.RemoteIterator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;

/*
 *  1. get Client Object
 *  2. ops Command
 *  3. close
 * */

public class HDFSClient {

    FileSystem fs;

    @Before
    public void setFileSystem() throws URISyntaxException, IOException {
        URI uri = new URI("hdfs://hadoop2:8020");
        Configuration conf = new Configuration();

        // hdfs-default.xml < hdfs-site < jar.resource.hdfs-site.xml < jar.class.configuration
        conf.set("dfs.replication", "2");
        try {
            this.fs = FileSystem.get(uri, conf,"root");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @After
    public void close() throws IOException {
        this.fs.close();
    }

    @Test
    public void testMkdir() throws IOException {
        this.fs.mkdirs(new Path("/xiyou/huaguoshan"));
        this.fs.close();
    }

    @Test
    public void testPut() {
        Collection<File> files = FileUtils.listFiles(new File("./data"), new String[]{"txt"}, true);
        files.stream().forEach(
                e -> {
                    try {
                        fs.copyFromLocalFile(false, true, new Path(e.getPath()), new Path("/sidamingzhu/data/" + e.getName()));
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                }
        );

    }


    @Test
    public void testGet() {
        try {

            FileUtils.deleteDirectory(new File("./data/xiyou"));
            fs.copyToLocalFile(false, new Path("/xiyou/"), new Path("./data/"), false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testFileDetail() {

        try {
            RemoteIterator<LocatedFileStatus> files = fs.listFiles(new Path("/"), true);

            while (files.hasNext()) {
                LocatedFileStatus fileStatus = files.next();

                System.out.println(fileStatus.getPath());
                System.out.println(fileStatus.getPermission());
                System.out.println(fileStatus.getBlockLocations());
                System.out.println(fileStatus.getBlockSize());
                System.out.println(fileStatus.getReplication());
                System.out.println(fileStatus.getOwner());

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
