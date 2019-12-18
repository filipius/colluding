function [nr,m,v] = clusterstats(x,c,K)
% CLUSTERSTATS(x,c) computes the statistics for each cluster
% [nr,m,v] = clusterstats(x,c[,K])
%	x  - d*n matrix of samples
%	c  - 1*n matrix with the cluster identity for each sample x(:,i)
%	K  - the number of different clusters (max(c) if omitted)
% returns
%	nr - 1*K matrix with the number of samples in each cluster
%	m  - d*K matrix with the mean for each cluster
%	v  - d*K matrix with the variance for each component

% Copyright (c) 1995-2001 Frank Dellaert
% All rights Reserved

if (nargin<3) K=max(c);end

% get dimensions of data
[d,n] = size(x);

% allocate space
nr = zeros(1,K);
sum = zeros(d,K);
sumsq = zeros(d,K);

% calculate stats
for i=1:K
   ci=find(c==i);
   nr(i)=length(ci);
   xi=x(:,ci);
   m(:,i)=mean(xi,2);
   v(:,i)=std(xi,1,2).^2;
end



