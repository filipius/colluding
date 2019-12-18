function selectdemo(seed)
% selectdemo: demonstrate mixtureSelect

% Copyright (c) 2001 Frank Dellaert
% All rights Reserved

if nargin<1,seed=0;end
rand ('state',seed);
randn('state',seed);

sigma=1;
maxK=7;

% generate data from 3 components
data = [randn(1,30) 3+randn(1,30) 5+randn(1,30)];

[K,c,z,pi,w] = mixtureSelect(data,maxK,sigma);
K
z
pi'
fprintf(1,'%d',c);
fprintf(1,'\n');

% add 2 components
data = [data  8+randn(1,30)  10+randn(1,30)];

[K,c,z,pi,w] = mixtureSelect(data,maxK,sigma);
K
z
pi'
fprintf(1,'%d',c);
fprintf(1,'\n');
