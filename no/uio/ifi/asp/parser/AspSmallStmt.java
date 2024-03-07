// © 2021 Dag Langmyhr, Institutt for informatikk, Universitetet i Oslo

package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

abstract class AspSmallStmt extends AspSyntax {

    AspSmallStmt(int n) {
        super(n);
    }

    static AspSmallStmt parse(Scanner s) {

        // -- Must be changed in part 2:
        enterParser("small stmt");

        AspSmallStmt ss = null;
        TokenKind cur = s.curToken().kind;

        if (cur == globalToken) {
            ss = AspGlobalStmt.parse(s);
        }

        else if (cur == passToken) {
            ss = AspPassStmt.parse(s);
        }

        else if (cur == returnToken) {
            ss = AspReturnStmt.parse(s);
        }

        else if (cur == nameToken && s.anyEqualToken()) {
            ss = AspAssignment.parse(s);
        }

        else {
            ss = AspExprStmt.parse(s);
        }

        leaveParser("small stmt");
        return ss;
    }
}
