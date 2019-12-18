function [c,z,pi,w,Q] = mixtureEM(data,K,sigma,z0,feedback)
% mixtureEM : cluster by estimating a mixture of Gaussians
% [c,z,pi,w,Q] = mixtureEM(data,K,sigma,z0,feedback)
%	data     - d*n samples
%	K        - number of clusters wanted
%   sigma    - standard deviation of components
%	z0	     - d*K initial guess for component means
%   feedback - function feedback(i,data,stats,theta,Q)
% returns:
%	c  - 1*n calculated membership vector where c(j) \in 1..K
%	z  - d*K cluster centroids
%   pi - K*1 mixture coefficients
%   w  - K*n soft assignment matrix
%   Q  - maximized expected log likelihood

% Copyright (c) 2001 Frank Dellaert
% All rights Reserved

% Check arguments
if ndims(data)~=2, error('data must be a matrix'); end
[d,n] = size(data);
if ~isequal(size(K),[1 1]), error('K must be a scalar');end
K=round(K);
if (K<1 | K>n), error('K must be at least one and at most n'); end
if nargin<4,z0=[];end
if nargin<5,feedback=@dummy;end

% initialize cluster centers and mixture coeff
permutation=randperm(n);
if isempty(z0)
   theta0.z = data(:,permutation(1:K));
else
   theta0.z = z0;
end
theta0.pi = ones(K,1)/K;

% call EM
parameters.Eparameters.sigma=sigma;
parameters.Mparameters.sigma=sigma;
[theta,w,Q] = EM(theta0,data,@Estep,@Mstep,feedback,parameters);

% w are the soft assignments, turn into crisp assignment
[maxw,c]=max(w);

z=theta.z;
pi=theta.pi;

%------------------------------------------------------------
% Estep
function w = Estep(theta,data,parameters)

% calculate log-likelihood for all K*n possible assignments
E = sqrDist(data,theta.z);

% turn into unnormalized posterior probability q
% taking some care to avoid very small numbers
loglikelihood=-0.5*E/parameters.sigma^2;
maxll=max(loglikelihood);
K=size(E,1);
likelihood=exp(loglikelihood-ones(K,1)*maxll);
q=full(spdiags(theta.pi,0,K,K)*likelihood);

% normalize
w = q./(ones(K,1)*sum(q));

%------------------------------------------------------------
% Mstep
function [theta,Q] = Mstep(theta,data,w,parameters)

% re-estimate component means by weighted average
K=size(theta.z,2);
for i=1:K
   wi=transpose(w(i,:));
   theta.z(:,i) = data * wi / sum(wi); % d*1 = d*n * n*1 / 1*1
end

% re-estimate mixture coefficients
theta.pi=sum(w,2);
theta.pi=theta.pi/sum(theta.pi);

% calculate Q
% should not have to do this twice (next time in E-step, I mean)
E = sqrDist(data,theta.z); % K*n
Q = -0.5*sum(sum(E.*w))/parameters.sigma^2 + sum(log(theta.pi).*sum(w,2));

%------------------------------------------------------------
% dummy feedback
function dummy(i,data,w,theta,Q)
%------------------------------------------------------------


