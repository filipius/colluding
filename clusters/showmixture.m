function showmixture(x,z,sigma)
% showmixture: project data matrix on first eigenvectors (if necessary)
%              and show Gaussian mixture in that space
% showmixture(x,z,sigma)
%	x     - data
%	z     - d*K cluster centroids
%   sigma - standard deviation of components

% Copyright (c) 2001 Frank Dellaert
% All rights Reserved

%----------------------------------------------------------------------
% first, coerce the data into displayable space, i.e. <= 3D, using PCA
%----------------------------------------------------------------------
[d,n] = size(x);
K=size(z,2);
if (d==1)
  projected(1,:) = 1:n;
  projected(2,:) = x;
  means = zeros(2,K);
  means(2,:)=z;
  d = 2;
elseif (d<=3)
  projected = x;
  means = z;
else
  [projected,mu,vc,ev] = projectpca(x,3);
  means = projectpca(z,mu,vc,ev);
  d = 3;
end;

%----------------------------------------------------------------------
% now, plot the data and ellipsoids in the space
%----------------------------------------------------------------------

% plot the data in 2d or 3d
if (d==2)
   plot(projected(1,:), projected(2,:), '.');
   hold on
   plot(means(1,:), means(2,:), 'r*');
else
   plot3(projected(1,:), projected(2,:), projected(3,:), '.');
   hold on
   plot3(means(1,:), means(2,:), means(3,:), 'r*');
end
rotate3d on

