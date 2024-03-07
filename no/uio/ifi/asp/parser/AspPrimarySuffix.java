// © 2021 Dag Langmyhr, Institutt for informatikk, Universitetet i Oslo

package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

abstract class AspPrimarySuffix extends AspSyntax {

    AspPrimarySuffix(int n) {
        super(n);
    }

    static AspPrimarySuffix parse(Scanner s) {

        // -- Must be changed in part 2:
        enterParser("primary suffix");

        AspPrimarySuffix ps = null;
        TokenKind cur = s.curToken().kind;

        if (cur == leftParToken) {
            ps = AspArguments.parse(s);
        }

        else if (cur == leftBracketToken) {
            ps = AspSubscription.parse(s);
        }

        else {
            parserError("Expected ( or [ token but found" + cur, s.curLineNum());
        }

        leaveParser("primary suffix");
        return ps;
    }
}
