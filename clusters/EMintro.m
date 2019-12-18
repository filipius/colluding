function EMintro(seed)
% EMintro : an introduction to EM as lower bound maximization
%           uses a 1D mixture with two components, equal priors
%           at each timestep, a lower bound is computed and plotted
% EMintro(seed)
%   seed: a scalar random generator seed value

% Below we have
%	K        - number of clusters
%   n        - number of datapoints per cluster
%	data     - d*n samples
%   sigma    - standard deviation of components
%	theta    - d*K cluster centroids
%   w        - K*n soft assignment matrix
%	c        - 1*n calculated membership vector where c(j) \in 1..K

% Copyright (c) 2001 Frank Dellaert
% All rights Reserved

if nargin<1,seed=2;end
rand ('state',seed);
randn('state',seed);

global EMintroformat surfHandle lastQ truelog
lastQ=[];
surfHandle=[];
%EMintroformat='-deps2';
%EMintroformat='-djpeg99'
EMintroformat=[];

% first, generate 1D data from 2 clusters
K=2;
n=3;
sigma=1;
data = [sigma*randn(1,n)-2 sigma*randn(1,n)+2];

parameters.maxIterations=5;
parameters.Eparameters.sigma=sigma;

% show the true mixture and the datapoints
range=-5:0.2:5;
figure(5);clf;set(5,'pos',[400 300 600 200]);
plot(data(1:n),zeros(1,n),'o');hold on;
plot(data((1:n)+n),zeros(1,n),'v');hold on;
plot(range,exp(-0.5*(range-2).^2));
plot(range,exp(-0.5*(range+2).^2));
axis([-5 5 -0.1 1]);
if ~isempty(EMintroformat)
   print('-f5',EMintroformat,'EMmixture');
end

% prepare approximation plot
figure(1);clf;set(1,'pos',[400 300 600 400]);
hold on;
view(3);
set(gca,'XGrid','on','YGrid','on','ZGrid','on');
xlabel('\theta_1');
ylabel('\theta_2');

% prepare membership plot
figure(2);clf;set(2,'pos',[400 150 600 120]);

% prepare 1D approximation plot
figure(3);clf;set(3,'pos',[0 300 400 300]);hold on

% calculate ground truth
range=-3:0.2:3;
[theta1,theta2]=meshgrid(range);
for i=1:length(range)
   for j=1:length(range)
      w = Estep([theta1(i,j) theta2(i,j)],data,parameters.Eparameters);
      truelog(i,j) = loglikelihood(data,w,theta1(i,j),theta2(i,j));
   end
end

% plot ground truth
figure(4);clf;set(4,'pos',[400 300 600 400]);
hold on;
view(3);
set(gca,'XGrid','on','YGrid','on','ZGrid','on');
xlabel('\theta_1');
ylabel('\theta_2');
surf(theta1,theta2,logORexp(truelog));
%shading('flat')
if(logORexp(0)==0)
   axis([-3 3 -3 3 0 40])
else
   axis([-3 3 -3 3 0 0.5])
end
if ~isempty(EMintroformat)
   print('-f4',EMintroformat,'EMtruth');
end

if 0
   % plot transparent ground truth on approximation plot
   figure(1)
   surf(theta1,theta2,logORexp(truelog));
   shading('interp');
   alpha(0.2);
end

% initialize means randomly
theta=[-0.1 0.1];

% call EM
[theta,w] = EM(theta,data,@Estep,@Mstep,@feedback,parameters);

if 0
   waitforbuttonpress

   % initialize means at the other side
   theta=[0.1 -0.1];
   
   % call EM
   parameters.Eparameters.sigma=sigma;
   [theta,w] = EM(theta,data,@Estep,@Mstep,@feedback,parameters);
end

%------------------------------------------------------------
% Estep
function w = Estep(theta,data,parameters)

% calculate log-likelihood for all K*n possible assignments
E = sqrDist(data,theta);

% turn into unnormalized posterior probability q
q = exp(-0.5*E/parameters.sigma^2);

% normalize
K=size(q,1);
w = q./(ones(K,1)*sum(q));

%------------------------------------------------------------
% Mstep
function [theta,Q] = Mstep(theta0,data,w,parameters)

figure(1);
if 0
   % plot touching point
   Qtouch = loglikelihood(data,w,theta0(1),theta0(2));
   plot3(theta0(1),theta0(2),logORexp(Qtouch),'r*');
end

% plot the lower-bound (assumes sigma=1)
range=-3:0.2:3;
[theta1,theta2]=meshgrid(range);
[Q,theta]=loglikelihood(data,w,theta1,theta2);

if 0 % plot bound gap instead (only makes sense with log plot)
   global truelog
   Q=Q-truelog;
end

global surfHandle
if ~isempty(surfHandle), delete(surfHandle);end
surfHandle=surf(theta1,theta2,logORexp(Q));
%set(surfHandle,'edgecolor',[0.5 0.5 0.5]);
if(logORexp(0)==0)
   axis([-3 3 -3 3 0 40])
else
   axis([-3 3 -3 3 0 0.5])
end
%shading('flat')
%alpha(0.2)

Q1=loglikelihood(data,w,range,theta(2)*ones(size(range)));
Q2=loglikelihood(data,w,theta(1)*ones(size(range)),range);
figure(1);
zero=zeros(size(range));
one=ones(size(range));
plot3(range,3*one,logORexp(Q1),'-.');
plot3(3*one,range,logORexp(Q2),'-.');
figure(3);
plot(range,logORexp(Q1),'-');
plot(range,-logORexp(Q2),'-');

% calculate Q
% should not have to do this twice (next time in E-step, I mean)
E = sqrDist(data,theta); % K*n

Q = loglikelihood(data,w,theta(1),theta(2));

figure(1);
plot3(theta(1),theta(2),logORexp(Q),'k*');
plot3(theta(1),3,logORexp(Q),'r*');
plot3(3,theta(2),logORexp(Q),'g*');
figure(3);
plot(theta(1), logORexp(Q),'r*');
plot(theta(2),-logORexp(Q),'g*');
drawnow
waitforbuttonpress


%------------------------------------------------------------
% dummy feedback
function feedback(i,data,w,theta,Q)

% priont iteration and Q
fprintf(1,'i=%d, Q=%f\n',i,Q);

global EMintroformat

% print approximation
figure(1)
title(sprintf('i=%d, Q=%f\n',i,Q));
if ~isempty(EMintroformat)
   print('-f1',EMintroformat,sprintf('EMbound%d',i))
end

% plot membership
figure(2);imagesc(w);colormap(hot)
if ~isempty(EMintroformat)
   print('-f2',EMintroformat,sprintf('EMmarginals%d',i))
end

drawnow

%------------------------------------------------------------
% control whether we show likelihoods or log-likelihoods
function y = logORexp(x)
%y=-x;
y=exp(x);

%------------------------------------------------------------
function [Q,x]=loglikelihood(data,w,theta1,theta2)

% calculate the lower-bound (assumes sigma=1)
% K*exp(Q) is Gaussian shaped, but does not integrate to 1 !
% (even with appropriate constants, which are omitted here)
% Q = \sumi \sumj w(i,j) * \log P(xj|zi)
% -2Q = \sumi \sumj w(i,j) * (xj-zi)^2
%     = \sumi \sumj w(i,j) * (xj^2-2*xj*zi+zi^2)
%     = \sumi si*[(\sumj w(i,j)*xj^2/si) -2*xi*zi + zi^2]
% where si = \sumj w(i,j) and xi = \sumj w(i,j)*xj / si
%     = \sumi si*[(\sumj w(i,j)*xj^2/si) -xi^2 + (xi-zi)^2]
%     = V + \sumi si*(xi-zi)^2
% where V = \sumi [(\sumj w(i,j)*xj^2)-si*xi^2]

% calculate Q
data2=data.^2;
V=0;
for i=1:2
   s(i) = sum(w(i,:));
   x(i) = w(i,:)*data'/s(i);
   V(i) = w(i,:)*data2'-s(i)*x(i)^2;
end
% we need to add the entropy to Q
% H = - \sumi \sumj w(i,j) * \log w(i,j)
H = - sum(sum(w.*log(w)));
Q=H-0.5*(s(1)*(theta1-x(1)).^2 + s(2)*(theta2-x(2)).^2 + sum(V));
