package com.vv.support.pipeline;

import java.util.List;

/**
 * 执行器
 * 将职责链串起来
 */
public class ProcessTemplate {
    private List<BusinessProcess> processList;

    public List<BusinessProcess> getProcessList(){
        return processList;
    }

    public void setProcessList(List<BusinessProcess> processList) {
        this.processList = processList;
    }
}
