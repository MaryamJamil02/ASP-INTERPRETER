package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;

public class AspFactorPrefix extends AspSyntax {

    TokenKind fpVal;

    protected AspFactorPrefix(int n) {
        super(n);
    }

    static AspFactorPrefix parse(Scanner s) {

        enterParser("factor prefix");

        AspFactorPrefix fp = new AspFactorPrefix(s.curLineNum());
        fp.fpVal = s.curToken().kind;
        skip(s, fp.fpVal);

        leaveParser("factor prefix");
        return fp;
    }

    @Override
    void prettyPrint() {
        prettyWrite("" + fpVal + " ");
    }

    @Override
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        return null; // Only terminals -> Returns null
    }
}
