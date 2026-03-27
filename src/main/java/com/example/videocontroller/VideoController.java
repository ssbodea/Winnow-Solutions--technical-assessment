package com.example.videocontroller;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Map;

@RestController
@RequestMapping("/video")
public class VideoController {

    @Value("${vlc.path}")
    private String vlcPath;
    @Value("${vlc.rc-port}")
    private int rcPort;
    private Process vlcProcess;

    private void startVLC(String file) throws IOException {
        if (vlcProcess != null && vlcProcess.isAlive()) {
            vlcProcess.destroy();
        }

        vlcProcess = new ProcessBuilder(
                vlcPath,
                "--extraintf", "rc",
                "--rc-host", "localhost:" + rcPort,
                "--no-video-title-show",
                file
        ).start();
    }

    private void sendCommand(String cmd) {
        try (Socket socket = new Socket("localhost", rcPort);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {
            out.println(cmd);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @PostMapping("/play")
    public String play(@RequestBody Map<String, String> body) {
        String file = body.get("file");
        try {
            startVLC(file);
        } catch (IOException e) {
            e.printStackTrace();
            return "Failed to start video";
        }
        return "Playing video: " + file;
    }

    @PostMapping("/pause")
    public String pause() {
        sendCommand("pause");
        return "Paused video";
    }

    @PostMapping("/stop")
    public String stop() {
        sendCommand("stop");
        if (vlcProcess != null && vlcProcess.isAlive()) {
            vlcProcess.destroy();
        }
        return "Stopped video";
    }
}