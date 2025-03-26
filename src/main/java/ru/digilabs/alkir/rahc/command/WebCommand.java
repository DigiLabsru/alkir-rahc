package ru.digilabs.alkir.rahc.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import picocli.CommandLine.Command;

import java.util.concurrent.Callable;

@Command(
    name = "web",
    description = "Run RAHC Web server",
    usageHelpAutoWidth = true,
    mixinStandardHelpOptions = true,
    sortOptions = false,
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
