// © 2021 Dag Langmyhr, Institutt for informatikk, Universitetet i Oslo

package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.scanner.*;

abstract class AspAtom extends AspSyntax {

    static AspAtom aa;

    AspAtom(int n) {
        super(n);
    }

    static AspAtom parse(Scanner s) {

        // -- Must be changed in part 2:
        enterParser("atom");

        switch (s.curToken().kind) {

            case falseToken:

            case trueToken:
                aa = AspBooleanLiteral.parse(s);
                break;

            case floatToken:
                aa = AspFloatLiteral.parse(s);
                break;

            case integerToken:
                aa = AspIntegerLiteral.parse(s);
                break;

            case leftBraceToken:
                aa = AspDictDisplay.parse(s);
                break;

            case leftBracketToken:
                aa = AspListDisplay.parse(s);
                break;

            case leftParToken:
                aa = AspInnerExpr.parse(s);
                break;

            case nameToken:
                aa = AspName.parse(s);
                break;

            case noneToken:
                aa = AspNoneLiteral.parse(s);
                break;

            case stringToken:
                aa = AspStringLiteral.parse(s);
                break;

            default:
                parserError("Expected an expression atom but found a " + s.curToken().kind + "!", s.curLineNum());
        }

        leaveParser("atom");

        return aa;
    }
}
