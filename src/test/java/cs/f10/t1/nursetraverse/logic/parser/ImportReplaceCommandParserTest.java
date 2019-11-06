package cs.f10.t1.nursetraverse.logic.parser;

import org.junit.jupiter.api.Test;

import cs.f10.t1.nursetraverse.logic.commands.ImportReplaceCommand;

public class ImportReplaceCommandParserTest {

    private ImportReplaceCommandParser parser = new ImportReplaceCommandParser();

    @Test
    public void parse_validArgs_returnsImportReplaceCommand() {
        // valid file name
        CommandParserTestUtil.assertParseSuccess(parser, " n/filename",
                new ImportReplaceCommand("filename"));
    }

    @Test
    public void parse_invalidArgs_throwsParseException() {
        // invalid file name
        CommandParserTestUtil.assertParseFailure(parser, " n/`/\\:;.@#",
                String.format(ParserUtil.MESSAGE_INVALID_FILENAME, "`/\\:;.@#"));
    }
}
