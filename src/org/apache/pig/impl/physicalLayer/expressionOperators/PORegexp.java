/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.pig.impl.physicalLayer.expressionOperators;

import java.util.regex.PatternSyntaxException;

import org.apache.pig.backend.executionengine.ExecException;
import org.apache.pig.data.DataByteArray;
import org.apache.pig.impl.plan.OperatorKey;
import org.apache.pig.impl.physicalLayer.POStatus;
import org.apache.pig.impl.physicalLayer.Result;
import org.apache.pig.impl.physicalLayer.plans.ExprPlanVisitor;
import org.apache.pig.impl.plan.VisitorException;

public class PORegexp extends BinaryComparisonOperator {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public PORegexp(OperatorKey k) {
        this(k, -1);
    }

    public PORegexp(OperatorKey k, int rp) {
        super(k, rp);
    }

    @Override
    public void visit(ExprPlanVisitor v) throws VisitorException {
        v.visitRegexp(this);
    }

    @Override
    public String name() {
        return "Matches - " + mKey.toString();
    }

    @Override
    public Result getNext(DataByteArray inp) throws ExecException {
        // TODO, no idea how to take this on.
        return new Result();
    }

    @Override
    public Result getNext(String inp) throws ExecException {
        byte status;
        Result res;

        String left = null, right = null;

        res = lhs.getNext(left);
        status = res.returnStatus;
        if (status != POStatus.STATUS_OK) {

            return res;
        }
        left = (String) res.result;

        res = rhs.getNext(right);
        status = res.returnStatus;
        if (status != POStatus.STATUS_OK) {

            return res;
        }
        right = (String) res.result;

        // left is expression to match against, right is regular expression
        try {
            res.result = new Boolean(left.matches(right));
            return res;
        } catch (PatternSyntaxException pse) {
            throw new ExecException("Unable to parse regular expression " +
                right, pse);
        }
    }

}
