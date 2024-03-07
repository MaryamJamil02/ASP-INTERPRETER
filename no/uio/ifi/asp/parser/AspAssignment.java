package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

import java.util.ArrayList;

public class AspAssignment extends AspSmallStmt {

    AspName name;
    ArrayList<AspSubscription> subscriptions = new ArrayList<>();
    AspExpr expr;

    AspAssignment(int n) {
        super(n);
    }

    static AspAssignment parse(Scanner s) {
        enterParser("assignment");

        AspAssignment a = new AspAssignment(s.curLineNum());
        a.name = AspName.parse(s);

        while (s.curToken().kind != equalToken) {
            a.subscriptions.add(AspSubscription.parse(s));
        }

        skip(s, equalToken);

        a.expr = AspExpr.parse(s);

        leaveParser("assignment");
        return a;
    }

    @Override
    void prettyPrint() {

        name.prettyPrint();

        for (AspSubscription as : subscriptions) {
            as.prettyPrint();
        }

        prettyWrite(" = ");
        expr.prettyPrint();
    }

    @Override
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        RuntimeValue v = expr.eval(curScope);

        if (subscriptions.isEmpty()) {
            curScope.assign(name.name, v);
            trace(name.name + " = " + v.toString());
        }

        // if subscriptions contains something
        else {
            RuntimeValue a = name.eval(curScope);

            // loop through subscriptions without last element
            for (int i = 0; i < subscriptions.size() - 1; i++) {
                a = a.evalSubscription(subscriptions.get(i).eval(curScope), this);
            }

            RuntimeValue lastElement = subscriptions.get(subscriptions.size() - 1).eval(curScope);
            a.evalAssignElem(lastElement, v, this);
            trace(name.name + "[" + lastElement.toString() + "] = " + v.toString());
            return v;
        }
        return v;
    }

}