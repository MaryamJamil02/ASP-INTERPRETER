package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspNotTest extends AspSyntax {

    AspComparison comparison;
    Boolean not = false;

    protected AspNotTest(int n) {
        super(n);
    }

    static AspNotTest parse(Scanner s) {

        enterParser("not test");

        AspNotTest nt = new AspNotTest(s.curLineNum());

        if (s.curToken().kind == notToken) {
            skip(s, notToken);
            nt.not = true;
        }

        nt.comparison = AspComparison.parse(s);

        leaveParser("not test");
        return nt;
    }

    @Override
    void prettyPrint() {

        if (not) {
            prettyWrite("not ");
        }
        comparison.prettyPrint();
    }

    @Override
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        RuntimeValue v = comparison.eval(curScope);
        
        if (not) {
            v = v.evalNot(this);
        }
        return v;
    }
}
