package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspIntegerLiteral extends AspAtom {

    long integer;

    AspIntegerLiteral(int n) {
        super(n);
    }

    static AspIntegerLiteral parse(Scanner s) {

        enterParser("integer literal");

        AspIntegerLiteral il = new AspIntegerLiteral(s.curLineNum());
        il.integer = s.curToken().integerLit;
        skip(s, integerToken);

        leaveParser("integer literal");
        return il;
    }

    @Override
    void prettyPrint() {
        prettyWrite("" + integer + "");
    }

    @Override
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        return new RuntimeIntValue(integer);
    }
}
