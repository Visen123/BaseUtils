package com.yanyiyun.baseutils.activity.designPattern.commandMode;

public class LightOnCommand implements Command {

    private Lights lights;

    public LightOnCommand(Lights lights) {
        this.lights = lights;
    }

    @Override
    public void execute() {
        lights.on();
    }
}
