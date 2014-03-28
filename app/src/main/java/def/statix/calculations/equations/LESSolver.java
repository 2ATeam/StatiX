package def.statix.calculations.equations;

import def.statix.utils.MathUtils;

public class LESSolver {

    private class Matrix {
        private float[][] matrix;
        private int n; // rows [iterator column index]
        private int m; // columns [iterator row index]

        public Matrix(int n) {
            this(n, n);
        }

        public Matrix(int n, int m) {
            this.matrix = new float[n][m];
            this.n = n;
            this.m = m;
        }

        /**
         * Stretches rows by the specified increment value.
         */
        private void stretchRows(int increment) {
            if (increment <= 0) return;
            float[][] tmp = matrix;
            matrix = new float[n][m + increment];
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < m; j++) {
                    matrix[i][j] = tmp[i][j];
                }
            }
            this.m = m + increment;
        }

        /**
         * Stretches columns by the specified increment value.
         */
        private void stretchColumns(int increment) {
            if (increment <= 0) return;
            float[][] tmp = matrix;
            matrix = new float[n + increment][m];
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < m; j++) {
                    matrix[i][j] = tmp[i][j];
                }
            }
            this.n = n + increment;
        }

        public void setColumn(int index, float[] column) {
            assert MathUtils.isWithin(0, index, m - 1, true);
            float[] c = column;
            if (c.length > n)
                stretchColumns(column.length - n);
            else if (c.length < n) {
                c = new float[n];
                for (int i = 0; i < column.length; i++) {
                    c[i] = column[i];
                }
            }

            for (int i = 0; i < n; i++) {
                matrix[i][index] = c[i];
            }
        }

        public void setRow(int index, float[] row) {
            assert MathUtils.isWithin(0, index, n - 1, true);
            float[] r = row;
            if (r.length > m)
                stretchRows(row.length - m);
            else if (r.length < m) {
                r = new float[m];
                for (int i = 0; i < row.length; i++) {
                    r[i] = row[i];
                }
            }

            for (int i = 0; i < m; i++) {
                matrix[index][i] = r[i];
            }
        }

        public boolean isSquared() {
            return n == m;
        }

        /**
         * Recursive algorithm with Minors
         */
        private float determinant() {
            int n = matrix.length - 1;
            if (n < 0) return 0;
            float M[][][] = new float[n + 1][][];

            M[n] = matrix;  // init first, largest, M to a

            // create working arrays
            for (int i = 0; i < n; i++)
                M[i] = new float[i + 1][i + 1];

            return getDecDet(M, n);
        }

        private float getDecDet(float[][][] M, int m) {
            if (m == 0) return M[0][0][0];
            int e = 1;

            // init subarray to upper left mxm submatrix
            for (int i = 0; i < m; i++)
                for (int j = 0; j < m; j++)
                    M[m - 1][i][j] = M[m][i][j];
            float sum = M[m][m][m] * getDecDet(M, m - 1);

            // walk through rest of rows of M
            for (int i = m - 1; i >= 0; i--) {
                for (int j = 0; j < m; j++)
                    M[m - 1][i][j] = M[m][i + 1][j];
                e = -e;
                sum += e * M[m][i][m] * getDecDet(M, m - 1);
            } // end for each row of matrix

            return sum;
        }

        public Matrix clone() {
            Matrix tmp = new Matrix(n, m);
            for (int i = 0; i < n; i++) {
                tmp.matrix[i] = this.matrix[i].clone();
            }
            return tmp;
        }
    }

    /**
     * Solves given linear equations system
     */
    public float[] solve(LinearEquationsSystem les) {
        if (!les.canSolve()) return null;

        float[] bVector = les.getConstantTermsVector();
        float[] solution = new float[les.getDimension()];
        Matrix base = new Matrix(les.getDimension());
        for (int i = 0; i < les.getDimension(); ++i) {
            base.setRow(i, les.getEquationAt(i).getCoefficients());
        }
        float baseDet = base.determinant();
        if (baseDet == 0) return null; // no solution
        Matrix tmp; // matrix which will be used to calculate determinant for each unknown.
        float det = 0;
        for (int i = 0; i < les.getDimension(); ++i) {
            tmp = base.clone();
            tmp.setColumn(i, bVector);
            det = tmp.determinant();
            solution[i] = det / baseDet;
        }
        return solution;
    }


}
