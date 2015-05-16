/************************************************************************
* \brief: class to solve the least squares equations with the Gauss-    *
*         elimination algorithm
*																		*
* (c) copyright by Jörn Fischer											*
*                                                                       *																		* 
* @autor: Prof.Dr.Jörn Fischer											*
* @email: j.fischer@hs-mannheim.de										*
*                                                                       *
* @file : MainFrame.java                                                *
*************************************************************************/

public class EquationSolver {

	public double solution[];
	public double matrix[][];

	int MDims;

	/**
	 * @brief: Constructor to reserve Memory for the matrix
	 * @param dims
	 */
	EquationSolver(int dims){
		solution = new double[dims+1];
		matrix = new double[dims+1][dims+1];
		
		MDims = dims;
		resetMatrix();
	}

	/**
	 * fills the matrix with zeroes
	 */
	void resetMatrix()
	{
		for (int t=0;t<MDims;t++){
			for (int i=0;i<MDims+1;i++){
				matrix[t][i]=0;
			}
		}
	}

	/**
	 * @brief: solves the equation
	 */
	void Solve()
	{
	 int i,j,k,max;
	 double swap;

	 for (i=0;i<MDims;i++){

		 // look for highest value
		 max=i;
		 for (j=i+1;j<MDims;j++){
			 if ((matrix[j][i])*(matrix[j][i])>(matrix[max][i])*(matrix[max][i]))
				max=j;
		 }
		 // swap with line of highest value
		 for (k=i;k<=MDims;k++){
			 swap=matrix[i][k];
			 matrix[i][k]=matrix[max][k];
			 matrix[max][k]=swap;
		 }
		 // eliminate
		 for (j=i+1;j<MDims;j++){
			 for (k=MDims;k>=i;k--){
				 if (matrix[i][i]!=0){
					matrix[j][k]-=matrix[i][k]*matrix[j][i]/matrix[i][i];
				 }
				 else{
					 matrix[j][k]-=matrix[i][k]*matrix[j][i]/0.00000000000000000001;
				 }
			 }
		 }

	 }

	 for (j=0;j<MDims;j++)
		 solution[j]=0;

	 for (j=MDims-1;j>=0;j--){

		 // substitute
		 double sum = 0.0;
		 for (k=j+1;k<MDims;k++){
			 sum += matrix[j][k]*solution[k];
		 
		 }
		 if (matrix[j][j]!=0){
			solution[j]=(matrix[j][MDims]-sum)/matrix[j][j];
		 }else{
			solution[j]=(matrix[j][MDims]-sum)/0.000000000000000000001;
		 }
	 }

	}

	/**
	 * @brief: adds a new data point to the matrix
	 * @param inVector: the input vector for the linear mapping
	 * @param target: the target value for the linear mapping
	 */
	public void leastSquaresAdd(double inVector[], double target)
	{

		for (int t=0;t<MDims;t++){
			matrix[t][MDims] += 2.0*inVector[t]*target;
		}
		for (int t=0;t<MDims;t++){
			for (int r=0;r<MDims;r++){
				matrix[t][r] += 2.0*inVector[t]*inVector[r];
			}
		}
	}
}
