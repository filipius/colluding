function [p,mu,vc,ev] = projectpca(data,nr_or_mu,vc,ev)
% projectpca : project data matrix on first nr eigenvectors
%  After the call, p will have a unit covariance matrix (changed from previous version!)
% two syntaxes:
%   [p,mu,vc,ev] = projectpca(data,nr)
%	  data - d*n data matrix
%	  nr   - on how many eigenvectors will we project ?
% OR, if pca is aleady available:
%   [p,mu,vc,ev] = projectpca(data,mu,vc,ev)
%	  data - d*n data matrix
%     mu   - d*1  mean of the data
%	  vc   - d*nr first nr eigenvectors (optional)
%	  ev   - 1*nr first nr eigenvalues  (optional)
% output in both cases:
%	p	- nr*n resulting projection
%   mu  - d*1  mean of the data
%	vc	- d*nr first nr eigenvectors
%	ev	- 1*nr first nr eigenvalues

% Copyright (c) 1995-2001 Frank Dellaert
% All rights Reserved
%
% historical note
%  Rather than using chemometrics copyrighted pca:
%   [eigenvectors,eigenvalues] = pca(x,nr);
%   vc = eigenvectors(:,1:nr);
%   ev = eigenvalues(1:nr);
%  We now use MATLAB built-in svds.
%  Also, we now multiply by diag(ev.^-0.5*sqrt(n)) to get whitened data.

[d,n] = size(data);

switch nargin
case 2
   nr=nr_or_mu;
   mu = mean(data,2);
   [vc,S,dummy] = svds(cov(data',1),nr);
   ev=diag(S);
case 4
   mu=nr_or_mu;
otherwise
   error('incorrect number of arguments');
end
x = data - repmat(mu,1,n);
p = diag(ev.^-0.5) * vc' * x;
