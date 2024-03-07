package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspBooleanLiteral extends AspAtom {

    TokenKind booleanVal;
    boolean value;

    AspBooleanLiteral(int n) {
        super(n);
    }

    static AspBooleanLiteral parse(Scanner s) {

        enterParser("boolean literal");

        AspBooleanLiteral bl = new AspBooleanLiteral(s.curLineNum());

        bl.booleanVal = s.curToken().kind;

        if (bl.booleanVal == trueToken) {
            bl.value = true;
        }

        else {
            bl.value = false;
        }

        skip(s, bl.booleanVal);

        leaveParser("boolean literal");
        return bl;
    }

    @Override
    void prettyPrint() {
        prettyWrite(booleanVal.toString());
    }

    @Override
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        return new RuntimeBoolValue(value);
    }
}
