function [projected,mu,vc,vl] = showpca2(data);
% SHOWPCA2 : project data matrix on 2 first eigenvectors and show them
% projected = showpca2(data)
%	data      - the data
%	projected - the resulting projection
%   mu        - d*1  mean of the data
%	vc	      - d*nr first nr eigenvectors
%	vl	      - 1*nr first nr eigenvalues

% Copyright (c) 1995-2001 Frank Dellaert
% All rights Reserved

[projected,mu,vc,vl] = projectpca(data,2);
plot(projected(1,:), projected(2,:), 'y*');
