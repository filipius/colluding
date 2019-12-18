function [K,c,z,pi,w] = mixtureSelect(data,maxK,sigma,feedback)
% mixtureSelect : estimate a mixture with unknown K using BIC
%                 (Bayesian Information Criterion)
% [K,c,z,pi,w] = mixtureSelect(data,maxK,sigma,feedback)
%	data     - d*n samples
%   maxK     - will test K in 2..maxK
%   sigma    - standard deviation of components
%   feedback - EM function feedback(i,data,stats,theta,Q)
% returns:
%	K     - number of clusters found
%	c     - 1*n calculated membership vector where c(j) \in 1..K
%	z     - d*K cluster centroids
%   pi    - K*1 mixture coefficients
%   w     - K*n soft assignment matrix

% Copyright (c) 2001 Frank Dellaert
% All rights Reserved

% Check arguments
if ndims(data)~=2, error('data must be a matrix'); end
[d,n] = size(data);
if nargin<5,feedback=@dummy;end

z=cell(1,maxK);
for K=1:maxK
   if K==1
      z{K}=mean(data,2);
      E = sqrDist(data,z{K});
      Q(K)=-0.5*sum(sum(E))/sigma^2;
   else
      % restart EM a number of times
      [c,z{K},pi,w,Q(K)] = restartEM(5,data,K,sigma,[],feedback);
      % instead of
      % E = sqrDist(data,z{K});
      % Q(K) = -0.5*sum(sum(E.*w))/sigma^2;
      % we subtract mixture probability term from EM Q to get data term only
      Q(K)=Q(K)-sum(log(pi).*sum(w,2));
   end
end

% add BIC complexity penalty and get optimal number of components
penalty=log(n)*(1:maxK)*(d+1);
BIC=-2*Q+penalty;
[minBIC,K]=min(BIC);

myfigure('BIC');clf;
plot(-2*Q,'g');hold on;plot(penalty,'r');plot(BIC,'b');

if K==1
   c=ones(1,n);
   w=ones(1,n);
   z=z{1};
   pi=1;
else
   % run EM again for that K with initial estimate from first run
   [c,z,pi,w] = mixtureEM(data,K,sigma,z{K},feedback);
end

%------------------------------------------------------------
% dummy feedback
function dummy(i,data,w,theta,Q)
%------------------------------------------------------------


