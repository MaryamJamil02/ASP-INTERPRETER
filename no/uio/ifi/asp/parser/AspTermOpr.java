package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;

public class AspTermOpr extends AspSyntax {

    TokenKind toVal;

    protected AspTermOpr(int n) {
        super(n);
    }

    static AspTermOpr parse(Scanner s) {

        enterParser("term opr");

        AspTermOpr to = new AspTermOpr(s.curLineNum());

        to.toVal = s.curToken().kind;
        skip(s, to.toVal);

        leaveParser("term opr");
        return to;
    }

    @Override
    void prettyPrint() {
        prettyWrite(" " + toVal + " ");
    }

    @Override
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        return null; // Only terminals -> Returns null
    }
}
