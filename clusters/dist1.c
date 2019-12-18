/*
 * function D = dist1(x,t);
 * dist1 : calculate a nt*nx vector containing distances between all points
 *	       in x and all points in t. x and t must be row vectors !
 * D = dist1(x,t)
 *	x,t	- row vectors
 *	D	- the nt*nx result
 *
 * Copyright (c) 1995-2001 Frank Dellaert
 * All rights Reserved
 */

/******************************************************************************
 * dist1_c does not include mex.h !!!
 *****************************************************************************/

void dist1_c(const double *x, const double *t, double *D, int nt, int nx)
  {
  int i,j;
  for (j=0;j<nx;j++)
    for (i=0;i<nt;i++,D++)
      *D = x[j]-t[i];
  }

/******************************************************************************
 * mex part
 *****************************************************************************/

#include "mex.h"

mxArray *dist1(const mxArray *x, const mxArray *t)
  {
  mxArray *D;
  int dx=mxGetM(x),nx=mxGetN(x);
  int dt=mxGetM(t),nt=mxGetN(t);

  if (dx!=1 || dt!=1) mexErrMsgTxt("dist1 only takes row vectors");
  if (!mxIsDouble(x) || !mxIsDouble(t)) mexErrMsgTxt("dist1 only takes double vectors");
  
  /* create output matrix and call pure C */
  D = mxCreateDoubleMatrix(nt,nx,mxREAL);  
  dist1_c(mxGetPr(x), mxGetPr(t), mxGetPr(D), nt, nx);
  return D;
  }

void mexFunction(int nargout, mxArray *out[], int nargin, const mxArray	*in[])
  {
  if (nargin!=2 || nargout>1) mexErrMsgTxt("usage: D = dist1(x,t)");
  out[0] = dist1(in[0], in[1]);
  }
