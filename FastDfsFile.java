package com.linghong.molian.bean;

import java.io.Serializable;

/**
 * fastDfs文件类
 * @author luck_nhb
 */
public class FastDfsFile implements Serializable {
    private String name;
    private byte[] content;
    private String ext;

    /**
     * 构造函数
     * @param name     文件名称
     * @param content  内容（byte）
     * @param ext      文件类型
     */
    public FastDfsFile(String name, byte[] content, String ext) {
        super();
        this.name = name;
        this.content = content;
        this.ext = ext;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }
}
