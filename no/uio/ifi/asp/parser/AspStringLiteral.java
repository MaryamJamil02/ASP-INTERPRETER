package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspStringLiteral extends AspAtom {

    String stringlit;

    AspStringLiteral(int n) {
        super(n);
    }

    static AspStringLiteral parse(Scanner s) {

        enterParser("string literal");

        AspStringLiteral sl = new AspStringLiteral(s.curLineNum());
        sl.stringlit = s.curToken().stringLit;
        skip(s, stringToken);

        leaveParser("string literal");
        return sl;
    }

    @Override
    void prettyPrint() {
        prettyWrite("\"" + stringlit + "\"");
    }

    @Override
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        return new RuntimeStringValue(stringlit);
    }
}
