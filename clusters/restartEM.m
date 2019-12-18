function [c,z,pi,w,Q] = restartEM(nrRestarts,data,K,varargin)
% restartEM : cluster by estimating a mixture of Gaussians
% [c,z,pi,w,Q] = restartEM(nrRestarts,data,K,varargin)

for i=1:nrRestarts
   [kc,z0] = kmeans(data,K);
   [c{i},z{i},pi{i},w{i},Q(i)] = mixtureEM(data,K,varargin{:});
end
[Q,best]=max(Q);
c  = c {best};
z  = z {best};
pi = pi{best};
w  = w {best};
