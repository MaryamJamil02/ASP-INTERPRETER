package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;

public class AspCompOpr extends AspSyntax {

    TokenKind coVal;

    protected AspCompOpr(int n) {
        super(n);
    }

    static AspCompOpr parse(Scanner s) {

        enterParser("comp opr");

        AspCompOpr co = new AspCompOpr(s.curLineNum());
        co.coVal = s.curToken().kind;
        skip(s, co.coVal);

        leaveParser("comp opr");
        return co;
    }

    @Override
    void prettyPrint() {
        prettyWrite(" " + coVal + " ");
    }

    @Override
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        return null; // Only terminals -> Returns null
    }
}
