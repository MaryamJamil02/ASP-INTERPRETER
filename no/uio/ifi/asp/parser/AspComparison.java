package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.main.Main;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;

public class AspComparison extends AspSyntax {

    ArrayList<AspTerm> terms = new ArrayList<>();
    ArrayList<AspCompOpr> compOprs = new ArrayList<>();

    protected AspComparison(int n) {
        super(n);
    }

    static AspComparison parse(Scanner s) {

        enterParser("comparison");

        AspComparison c = new AspComparison(s.curLineNum());
        c.terms.add(AspTerm.parse(s));

        // As long as currentoken is a compOpr
        while (s.isCompOpr()) {
            c.compOprs.add(AspCompOpr.parse(s));
            c.terms.add(AspTerm.parse(s));
        }

        leaveParser("comparison");
        return c;
    }

    @Override
    void prettyPrint() {

        for (int i = 0; i < terms.size(); i++) {
            terms.get(i).prettyPrint();

            if (i <= compOprs.size() - 1) {
                compOprs.get(i).prettyPrint();
            }
        }
    }

    @Override
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        RuntimeValue v = terms.get(0).eval(curScope);

        for (int i = 1; i < terms.size(); ++i) {
            TokenKind k = compOprs.get(i - 1).coVal;

            if (i > 1) {
                v = terms.get(i - 1).eval(curScope);
            }

            switch (k) {
                case lessToken:
                    v = v.evalLess(terms.get(i).eval(curScope), this);
                    break;

                case greaterToken:
                    v = v.evalGreater(terms.get(i).eval(curScope), this);
                    break;

                case doubleEqualToken:
                    v = v.evalEqual(terms.get(i).eval(curScope), this);
                    break;

                case greaterEqualToken:
                    v = v.evalGreaterEqual(terms.get(i).eval(curScope), this);
                    break;

                case lessEqualToken:
                    v = v.evalLessEqual(terms.get(i).eval(curScope), this);
                    break;

                case notEqualToken:
                    v = v.evalNotEqual(terms.get(i).eval(curScope), this);
                    break;

                default:
                    Main.panic("Illegal term operator: " + k + "!");
            }

            // If false -> returns false
            // If true -> skips test and continues evaluating (loop)
            if (v.getBoolValue("comparison", this) == false) {
                return v;
            }
        }
        // Returns true -> each of the conditions are true
        return v;
    }
}