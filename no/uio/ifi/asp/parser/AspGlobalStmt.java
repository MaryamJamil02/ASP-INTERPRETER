package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspGlobalStmt extends AspSmallStmt {

    ArrayList<AspName> names = new ArrayList<>();

    AspGlobalStmt(int n) {
        super(n);
    }

    static AspGlobalStmt parse(Scanner s) {

        enterParser("global stmt");

        AspGlobalStmt gs = new AspGlobalStmt(s.curLineNum());

        skip(s, globalToken);

        while (true) {
            gs.names.add(AspName.parse(s));
            if (s.curToken().kind != commaToken) {
                break;
            }
            skip(s, commaToken);
        }

        leaveParser("global stmt");
        return gs;
    }

    @Override
    void prettyPrint() {

        int nPrinted = 0;

        prettyWrite("global ");

        for (AspName an : names) {
            if (nPrinted > 0) {
                prettyWrite(", ");
            }
            an.prettyPrint();
            ++nPrinted;
        }
    }

    @Override
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {

        for (AspName name : names) {
            curScope.registerGlobalName(name.name);;
        }
        return null;
    }
}