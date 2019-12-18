/*
 * SQRDIST : calculate a nt*nx matrix containing weighted squared error between
 *           every vector in x and every vector in t
 * D = sqrDist(x,t,w)
 *	x - d*nx matrix containing the x vectors 
 *	t - d*nt matrix containing the t vectors
 *	w - if specified, a length d weight vector
 *	D - the nt*nx result
 *
 * Copyright (c) 1995-2001 Frank Dellaert
 * All rights Reserved
 */

/******************************************************************************
 * weightedSqrDist_c and sqrDist_c do not include mex.h
 *****************************************************************************/

void weightedSqrDist_c(const double *x, const double *t, const double *w,
                       int nt, int nx, int d, double *D)
  {
  int i,j,k;
  const double *xj=x;
  for (j=0;j<nx;j++,xj+=d) {
    const double *ti=t;
    for (i=0;i<nt;i++,D++,ti+=d) {
      *D=0;
      for (k=0;k<d;k++) {
        double dk=w[k]*(xj[k]-ti[k]);
        *D += dk*dk;
        }
      }
    }
  }

void sqrDist_c(const double *x, const double *t,
               int nt, int nx, int d, double *D)
  {
  int i,j,k;
  const double *xj=x;
  for (j=0;j<nx;j++,xj+=d) {
    const double *ti=t;
    for (i=0;i<nt;i++,D++,ti+=d) {
      *D=0;
      for (k=0;k<d;k++) {
        double dk=xj[k]-ti[k];
        *D += dk*dk;
        }
      }
    }
  }

/******************************************************************************
 * mex part
 *****************************************************************************/

#include "mex.h"

mxArray *sqrDist(const mxArray *x, const mxArray *t, const mxArray *w)
  {
  mxArray *D;
  int dx=mxGetM(x),nx=mxGetN(x);
  int dt=mxGetM(t),nt=mxGetN(t);
  int d = (w==NULL) ? dt : mxGetN(w)*mxGetM(w);

  if (dt!=dx) mexErrMsgTxt("x and t must have same dimension");
  if ( d!=dx) mexErrMsgTxt("w has wrong number of elements");
  if (!mxIsDouble(x) || !mxIsDouble(t)) mexErrMsgTxt("sqrDist only takes double vectors");
  
  /* create output matrix and call pure C */
  D = mxCreateDoubleMatrix(nt,nx,mxREAL);
  if (w==NULL) sqrDist_c(mxGetPr(x), mxGetPr(t), nt, nx, dx, mxGetPr(D));
  else weightedSqrDist_c(mxGetPr(x), mxGetPr(t), mxGetPr(w), nt, nx, dx, mxGetPr(D));
  return D;
  }

void mexFunction(int nargout, mxArray *out[], int nargin, const mxArray	*in[])
  {
  if ((nargin!=2 && nargin!=3) || nargout>1) mexErrMsgTxt("usage: D = sqrDist(x,t,w)");
  out[0] = sqrDist(in[0], in[1], nargin == 3 ? in[2] : NULL);
  }
