package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspFloatLiteral extends AspAtom {

    double floatNum;

    AspFloatLiteral(int n) {
        super(n);
    }

    static AspFloatLiteral parse(Scanner s) {

        enterParser("float literal");

        AspFloatLiteral fl = new AspFloatLiteral(s.curLineNum());
        fl.floatNum = s.curToken().floatLit;
        skip(s, floatToken);

        leaveParser("float literal");
        return fl;
    }

    @Override
    void prettyPrint() {
        prettyWrite("" + floatNum + "");
    }

    @Override
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        return new RuntimeFloatValue(floatNum);
    }
}
