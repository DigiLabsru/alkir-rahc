package ru.digilabs.alkir.rahc.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.util.concurrent.Callable;

@Command(
    name = "web",
    description = "Run RAHC Web server",
    usageHelpAutoWidth = true,
    footer = "@|green Copyright(c) 2022-2025|@"
)
@Component
@RequiredArgsConstructor
public class WebCommand implements Callable<Integer> {

    @Override
    public Integer call() {
        return -1;
    }
}
