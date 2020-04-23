package com.yanyiyun.baseutils.activity.designPattern.commandMode;

/**
 * 调用者 Invoker
 */
public class RemoteControl {

    private Command[] onCommands;
    private Command[] offCommands;

    public RemoteControl() {
        onCommands=new Command[7];
        offCommands=new Command[7];

        NoCommand noCommand=new NoCommand();
        for(int i=0;i<7;i++){
            onCommands[i]=noCommand;
            offCommands[i]=noCommand;
        }
    }

    public void setCommand(int temp,Command onCommand,Command offCommand){
        onCommands[temp]=onCommand;
        offCommands[temp]=offCommand;
    }

    public void OnButtonWasPressed(int temp){
        onCommands[temp].execute();
    }

    public void offButtonWasPressed(int temp){
        offCommands[temp].execute();
    }
}
