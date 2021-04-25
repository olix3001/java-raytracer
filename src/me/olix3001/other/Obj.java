package me.olix3001.other;

public class Obj {
    private String objPath = null;
    private String mtlPath = null;

    public Obj(String objPath) {
        this.objPath = objPath;
    }

    public Obj(String objPath, String mtlPath) {
        this.objPath = objPath;
        this.mtlPath = mtlPath;
    }

    public String getObjPath() {
        return objPath;
    }

    public String getMtlPath() {
        return mtlPath;
    }

    public void setMtlPath(String mtlPath) {
        this.mtlPath = mtlPath;
    }

    public boolean hasMtl() {
        return !(mtlPath == null);
    }
}
