function D = manhattan(X,z,W);
% manhattan : calculate a 1*n vector D containing manhattan distances from z
%             to every vector in X
% D = manhattan(X,z,W)
%	X - d*n matrix containing the vectors
%	z - d*1 vector to compare all x's with
%	W - if specified, a d*d weight matrix
%	D - the 1*n result

% Copyright (c) 1995 Frank Dellaert
% All rights Reserved

[d,n] = size(X);

E = X-z*ones(1,n); % d*n matrix of error vectors

if (nargin>=3)
  E = W*E;
end

if (d>1)
  D = sum(abs(E))
else
  D = E;
end


