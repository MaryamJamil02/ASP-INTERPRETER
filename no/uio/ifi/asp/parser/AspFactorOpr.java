package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;

public class AspFactorOpr extends AspSyntax {

    TokenKind foVal;

    protected AspFactorOpr(int n) {
        super(n);
    }

    static AspFactorOpr parse(Scanner s) {

        enterParser("factor opr");

        AspFactorOpr fo = new AspFactorOpr(s.curLineNum());
        fo.foVal = s.curToken().kind;
        skip(s, fo.foVal);

        leaveParser("factor opr");
        return fo;
    }

    @Override
    void prettyPrint() {
        prettyWrite(" " + foVal + " ");
    }

    @Override
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        return null; // Only terminals -> Returns null
    }
}
