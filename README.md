# Kinetic - Floodlight OpenFlow Controller

## Environment Setup

VM

    Floodlight provides their VM where you can find it here https://floodlight.atlassian.net/wiki/spaces/floodlightcontroller/pages/8650780/Floodlight+VM#FloodlightVM-VMUsage

On your own machine:

    sudo apt-get install build-essential openjdk-7-jdk ant maven python-dev eclipse
    // Floodlight v1.2 requires JDK 7 so switch to jdk 7 after install it

## Build

    git clone https://github.com/elecoaries/floodlight.git
    cd floodlight
    git checkout v1.2-kinetic
    ant

    sudo mkdir /var/lib/floodlight
    sudo chmod 777 /var/lib/floodlight

    ant eclipse

## Run in Eclipse

    Open eclipse and create a new workspace File -> Import -> General -> Existing Projects into Workspace.
    Then click "Next". From "Select root directory" click "Browse".
    Select the parent directory where you placed floodlight earlier.
    Check the box for "Floodlight". No other Projects should be present and none should be selected. Click Finish.

    To create the floodlightLaunch target
        Click Run->Run Configurations
        Right Click Java Application->New
        For Name use FloodlightLaunch
        For Project use Floodlight
        For Main use net.floodlightcontroller.core.Main
        Click Apply

    To run it
        In package explorer, under src/main/java/net.floodlightcontroller.core, right click Main.java, choose run as Java application, use default config
        Then you can start mininet (need to use OpenFlow13) to test it
            i.e. sudo mn --topo single,3 --controller=remote,ip=127.0.0.1,port=6653 --switch ovsk,protocols=OpenFlow13
        Source code is located under src/main/java/net.floodlightcontroller.packethandler

## Generate SMV files from FSM.java

    Use terminal and go to the location of SMV_Generator.java (should be in `floodlight/smv_gen`)
    Run `javac SMV_Generator.java` to compile.
    Run `java SMV_Generator FSM.java > FSM.smv` to parse and generate the smv file from the FSM.java file in the same directory.
    Alternatively, supply a different FSM.java file by specifying the file path in the first argument.
