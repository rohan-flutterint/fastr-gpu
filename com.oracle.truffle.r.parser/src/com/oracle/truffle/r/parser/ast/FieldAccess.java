/*
 * This material is distributed under the GNU General Public License
 * Version 2. You may review the terms of this license at
 * http://www.gnu.org/licenses/gpl-2.0.html
 *
 * Copyright (c) 2012-2014, Purdue University
 * Copyright (c) 2013, 2014, Oracle and/or its affiliates
 *
 * All rights reserved.
 */
package com.oracle.truffle.r.parser.ast;

import java.util.*;

import com.oracle.truffle.api.source.*;

public class FieldAccess extends ASTNode {

    private final ASTNode lhs;
    private final Symbol fieldName;

    private FieldAccess(SourceSection source, ASTNode value, Symbol fieldName) {
        super(source);
        this.lhs = value;
        this.fieldName = fieldName;
    }

    @Override
    public <R> R accept(Visitor<R> v) {
        return v.visit(this);
    }

    @Override
    public <R> List<R> visitAll(Visitor<R> v) {
        return Arrays.asList(lhs.accept(v));
    }

    public static ASTNode create(SourceSection src, FieldOperator op, ASTNode value, String fieldName) {
        switch (op) {
            case AT: // // FIXME determine the meaning of AT in this case - falling back to put
            case FIELD:
                return new FieldAccess(src, value, Symbol.getSymbol(fieldName));
        }
        throw new Error("No node implemented for: '" + op + "' (" + value + ": " + fieldName + ")");
    }

    public ASTNode getLhs() {
        return lhs;
    }

    public Symbol getFieldName() {
        return fieldName;
    }
}
