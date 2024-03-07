package no.uio.ifi.asp.parser;

import java.util.ArrayList;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspSmallStmtList extends AspStmt {

    ArrayList<AspSmallStmt> smallStmts = new ArrayList<>();

    AspSmallStmtList(int n) {
        super(n);
    }

    static AspSmallStmtList parse(Scanner s) {

        enterParser("small stmt list");

        AspSmallStmtList ssl = new AspSmallStmtList(s.curLineNum());

        // Loops until it meets a newline token
        while (true) {

            ssl.smallStmts.add(AspSmallStmt.parse(s));

            if (s.curToken().kind != semicolonToken) {
                break;
            }

            if (s.curToken().kind == semicolonToken) {
                skip(s, semicolonToken);
            }

            if (s.curToken().kind == newLineToken) {
                break;
            }
        }
        skip(s, newLineToken);

        leaveParser("small stmt list");
        return ssl;
    }

    @Override
    void prettyPrint() {

        int nPrinted = 0;

        for (AspSmallStmt ass : smallStmts) {
            if (nPrinted > 0) {
                prettyWrite("; ");
            }
            ass.prettyPrint();
            ++nPrinted;
        }

        prettyWriteLn();
    }

    @Override
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {

        for (AspSmallStmt aspSmallStmt : smallStmts) {
            aspSmallStmt.eval(curScope);
        }
        return null;
    }
}