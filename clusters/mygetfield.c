/******************************************************************************
 * C version of mxGetField
 *****************************************************************************/

#include "mex.h"

mxArray* mygetfield(
  const mxArray* structPointer, 
  const mxArray* fieldnamePointer,
  const mxArray* defaultvalue
)
  {
  mxArray* result;

  char fieldName[100];
  int status = mxGetString(fieldnamePointer, fieldName, 100);
  if (status) mexErrMsgTxt("can't read string");
  
  result = mxGetField(structPointer, 0, fieldName);
  
  if (result==NULL) mxDuplicateArray(defaultvalue);
  }

/******************************************************************************
% test
 *****************************************************************************/

void mexFunction(int nargout, mxArray *out[], int nargin, const mxArray	*in[])
  {
  if (nargin!=3 | nargout>1)
    mexErrMsgTxt("usage: value=mygetfield(s,f,default)");
  out[0]=mygetfield(in[0],in[1],in[2]);
  }

/******************************************************************************/
