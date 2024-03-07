package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspWhileStmt extends AspCompoundStmt {

    AspExpr expr;
    AspSuite suite;

    AspWhileStmt(int n) {
        super(n);
    }

    static AspWhileStmt parse(Scanner s) {

        enterParser("while stmt");

        AspWhileStmt ws = new AspWhileStmt(s.curLineNum());

        skip(s, whileToken);
        ws.expr = AspExpr.parse(s);
        skip(s, colonToken);
        ws.suite = AspSuite.parse(s);

        leaveParser("while stmt");
        return ws;
    }

    @Override
    void prettyPrint() {
        prettyWrite("while ");
        expr.prettyPrint();
        prettyWrite(": ");
        suite.prettyPrint();
    }

    @Override
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        while (true) {
            RuntimeValue t = expr.eval(curScope);
            if (!t.getBoolValue("while loop test", this))
                break;
            trace("while True: ...");
            suite.eval(curScope);
        }
        trace("while False:");
        return null;
    }
}
