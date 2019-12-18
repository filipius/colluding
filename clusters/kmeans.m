function [c,mu] = kmeans(x,K,mu)
% kmeans : K-means clustering
% [c,mu] = kmeans(x,K)
%	x  - d*n samples
%	K  - number of clusters wanted
%	mu	- optional d*K initial guess for cluster centroids
% returns:
%	c  - 1*n calculated membership vector where c(j) \in 1..K
%	mu	- d*K cluster centroids
% algorithm taken from Sing-Tze Bow, 'Pattern Recognition'

% Copyright (c) 1995-2001 Frank Dellaert
% All rights Reserved

%------------------------------------------------------------------------
% Check arguments
%------------------------------------------------------------------------
if ndims(x)~=2, error('x must be a matrix'); end
[d,n] = size(x);
if ~isequal(size(K),[1 1]), error('K must be a scalar');end
K=round(K);
if (K<1 | K>n), error(sprintf('K must be at least one and at most n=%d',n)); end

%------------------------------------------------------------------------
% Initialize variables
%------------------------------------------------------------------------
oldmu = Inf*ones(d,K);
c = zeros(1,n);
D = zeros(K,n);

%------------------------------------------------------------------------
% step 1: Arbitrarily choose K samples as the initial cluster centers
%------------------------------------------------------------------------
if nargin<3
   p=randperm(n);
   mu = x(:,p(1:K));
end

while(1),
   %------------------------------------------------------------------------
   % step 2: distribute the samples x to the chosen cluster domains
   %         based on which cluster center is nearest
   %------------------------------------------------------------------------
   for j=1:K,                        % for every cluster
      center = mu(:,j);              % get cluster center
      if ~isequal(center,oldmu(:,j)) % has it moved ? 
         D(j,:) = sqrDist(x,center); % calculate sqrDist from x to center
      end
   end
   oldmu = mu;
   
   % find minimum
   [Dmin,index] = min(D);
   moved = sum(index~=c);
   %fprintf(2,'moved = %d\n',moved);
   c = index;
   
   %------------------------------------------------------------------------
   % step 3: Update the cluster centers
   %------------------------------------------------------------------------
   for i=1:K
      ci=find(c==i);
      mu(:,i)=mean(x(:,ci),2);
   end
   
   %------------------------------------------------------------------------
   % step 4: Check convergence
   %------------------------------------------------------------------------
   if (moved==0), break, end
   
   %------------------------------------------------------------------------
end % while(1)


