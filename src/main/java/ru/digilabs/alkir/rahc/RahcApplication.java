package ru.digilabs.alkir.rahc;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import ru.digilabs.alkir.rahc.command.CliCommand;
import ru.digilabs.alkir.rahc.command.WebCommand;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

@SpringBootApplication
@ConfigurationPropertiesScan
@RequiredArgsConstructor
@Command(
    name = "rahc",
    subcommands = {
        WebCommand.class,
        CliCommand.class
    },
    usageHelpAutoWidth = true,
    synopsisSubcommandLabel = "[COMMAND [ARGS]]",
    footer = "@|green Copyright(c) 2022-2025|@",
    header = "@|green Alkir RAHC|@")
public class RahcApplication implements ExitCodeGenerator {

    private static final String DEFAULT_COMMAND = "web";

    private final CommandLine.IFactory picocliFactory;

    @Getter
    private int exitCode;

    @CommandLine.Option(
        names = {"-h", "--help"},
        usageHelp = true,
        scope = CommandLine.ScopeType.INHERIT,
        description = "Show this help message and exit")
    private boolean usageHelpRequested;

    @CommandLine.Unmatched
    private List<String> unmatched;

    private final Set<Pattern> allowedAdditionalArgs = Set.of(
        Pattern.compile("--spring\\."),
        Pattern.compile("--app\\."),
        Pattern.compile("--logging\\."),
        Pattern.compile("--debug")
    );

    public static void main(String[] args) {

        var webApplicationType = getWebApplicationType(args);

        var applicationBuilder = new SpringApplicationBuilder(RahcApplication.class)
            .web(webApplicationType);

        switch (webApplicationType) {
            case SERVLET -> applicationBuilder.profiles("web");
            case NONE -> applicationBuilder.profiles("cli");
        }

        var applicationContext = applicationBuilder.run(args);

        var launcher = applicationContext.getBean(RahcApplication.class);
        launcher.run(args);

        if (launcher.getExitCode() >= 0) {
            System.exit(
                SpringApplication.exit(applicationContext)
            );
        }
    }

    public void run(String... args) {
        var cmd = new CommandLine(this, picocliFactory);

        // проверка использования дефолтной команды
        // если строка параметров пуста, то это точно вызов команды по умолчанию
        if (args.length == 0) {
            args = addDefaultCommand(args);
        } else {
            var parseResult = cmd.parseArgs(args);
            var unmatchedArgs = parseResult.unmatched().stream()
                .filter(s -> allowedAdditionalArgs.stream().noneMatch(pattern -> pattern.matcher(s).matches()))
                .toList();

            if (!unmatchedArgs.isEmpty()) {
                unmatchedArgs.forEach(s -> cmd.getErr().println("Unknown option: '" + s + "'"));
                cmd.usage(cmd.getOut());
                exitCode = cmd.getCommandSpec().exitCodeOnInvalidInput();
                return;
            }

            // если переданы параметры без команды и это не справка
            // то считаем, что параметры для команды по умолчанию
            if (!parseResult.hasSubcommand() && !parseResult.isUsageHelpRequested()) {
                args = addDefaultCommand(args);
            }
        }

        exitCode = cmd.execute(args);
    }

    private static String[] addDefaultCommand(String[] args) {
        List<String> tmpList = new ArrayList<>(Arrays.asList(args));
        tmpList.add(0, DEFAULT_COMMAND);
        args = tmpList.toArray(new String[0]);
        return args;
    }

    private static WebApplicationType getWebApplicationType(String[] args) {
        WebApplicationType webApplicationType = WebApplicationType.SERVLET;

        var argsList = Arrays.asList(args);
        if (argsList.contains("cli") || argsList.contains("-h") || argsList.contains("--help")) {
            webApplicationType = WebApplicationType.NONE;
        }

        return webApplicationType;
    }

}
