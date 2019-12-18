function emdemo(seed)
% EMDEMO: demonstrate EM clustering

% Copyright (c) 2001 Frank Dellaert
% All rights Reserved

if nargin<1,seed=3;end
rand ('state',seed);
randn('state',seed);

data = [randn(2,20) [3+randn(1,80);randn(1,80)]];
K=2;
sigma=1;

% plot data
figure(1);clf;set(1,'pos',[400 300 600 400]);
plot(data(1,:),data(2,:),'.');
hold on;

% prepare membership plot
figure(2);clf;set(2,'pos',[400 150 600 120]);

% prepare Q plot
figure(3);clf;set(3,'pos',[10 300 400 400]);hold on

% do EM
[c,z,pi,w] = mixtureEM(data,K,sigma,[],@feedback);
pi

% plot clusters
figure(1);
colors='rgbyk';
for i=1:K
   ji=find(c==i);
   plot(data(1,ji),data(2,ji),['.' colors(i)]);
end
plot(z(1,:),z(2,:),'co');

% also show K-means solution
[kc,kz] = kmeans(data,K,z);
plot(kz(1,:),kz(2,:),'bo');

%------------------------------------------------------------
% demo feedback
function feedback(i,data,w,theta,Q)
fprintf(1,'i=%d, Q=%f\n',i,Q);
figure(1);plot(theta.z(1,:),theta.z(2,:),'c.');
figure(2);imagesc(w);colormap(hot)
figure(3);plot(i,Q,'o')
E = sqrDist(data,theta.z); % K*n
plot(i,-0.5*sum(sum(E.*w)),'g.');
plot(i,sum(log(theta.pi).*sum(w,2)),'r.');
drawnow
