package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspNoneLiteral extends AspAtom {

    AspNoneLiteral(int n) {
        super(n);
    }

    static AspNoneLiteral parse(Scanner s) {

        enterParser("none literal");

        AspNoneLiteral nl = new AspNoneLiteral(s.curLineNum());
        skip(s, noneToken);

        leaveParser("none literal");
        return nl;
    }

    @Override
    void prettyPrint() {
        prettyWrite("none");
    }

    @Override
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        return new RuntimeNoneValue();
    }
}
