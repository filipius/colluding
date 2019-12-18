function [xj,I] = cluster(x,c,j);
% CLUSTER : return the matrix of samples in cluster j according to c
% [xj,I] = cluster(x,c,j)
%	x  - data
%	c  - categories
%	j  - cluster identity
%	xj - x samples in cluster j
%	I  - indices of these samples in x/c

% Copyright (c) 1995-1996 Frank Dellaert
% All rights Reserved

[dx,nx] = size(x);
[dc,nc] = size(c);

if (nc~=nx) error('cluster: x and c must have same number of columns'); end
if (dc~=1) error('cluster: c must be row vector'); end

I = find(c==j);
xj = x(:,I);
